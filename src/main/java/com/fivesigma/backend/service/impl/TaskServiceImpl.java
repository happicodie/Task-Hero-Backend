package com.fivesigma.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fivesigma.backend.dao.ITaskInfoDao;
import com.fivesigma.backend.dao.ITaskRelDao;
import com.fivesigma.backend.dto.TaskRelDTO;
import com.fivesigma.backend.po_entity.Log;
import com.fivesigma.backend.po_entity.TaskInfo;
import com.fivesigma.backend.po_entity.TaskRelation;
import com.fivesigma.backend.service.ITaskService;
import com.fivesigma.backend.util.DateUtil;
import com.fivesigma.backend.util.JWTUtil;
import com.fivesigma.backend.util.TaskComparator;
import com.fivesigma.backend.vo.*;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Andy
 * @date 2022/10/29
 */
@Service
public class TaskServiceImpl extends MPJBaseServiceImpl<ITaskRelDao, TaskRelation> implements ITaskService {
    @Autowired
    private ITaskInfoDao taskInfoDao;
    @Autowired
    private ITaskRelDao taskRelDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private DateUtil dateUtil;


    @Override
//    @Transactional
    public TaskCreateVO createTask(String token, TaskInfo_ReqVO taskInfo_reqVO) {
        token = token.replace("Bearer ", "");
        String assigner_id = jwtUtil.userIdInToken(token);

        //todo: insert into mongodb task_info document
        TaskInfo new_task = new TaskInfo(taskInfo_reqVO);
        new_task.setTask_id(RandomStringUtils.randomAlphabetic(10));
//
        boolean assignToMyself = taskInfo_reqVO.getAssignee_id().equals(assigner_id);
        //set task status base on start date (not null) and user id
        String start_date = new_task.getStart_date();
        if (DateUtil.isFuture(start_date) ||
            !assignToMyself){
            new_task.setTask_status("not started");
        }else {
            new_task.setTask_status("in progress");
        }

        List<Log> new_log = new ArrayList<>();
        new_task.setLogs(new_log);
        new_task.setTask_score(-1);
        new_task.setTask_feedback("unset");
        taskInfoDao.insert(new_task);
        //todo: update log
        String log_content = assigner_id + " assign task to " + taskInfo_reqVO.getAssignee_id();
        if (assigner_id.equals(taskInfo_reqVO.getAssignee_id())){
            log_content += " and accept";
        }
        List<Log> logs = writeLog(new_task.getTask_id(), log_content);

        //todo: insert into mysql task_relation table
        TaskRelation taskRelation = new TaskRelation();
        taskRelation.setTask_id(new_task.getTask_id());
        taskRelation.setAssigner_id(assigner_id);
        taskRelation.setAssignee_id(taskInfo_reqVO.getAssignee_id());
        if (assignToMyself) {
            taskRelation.setAccept(true);
        }else {
            taskRelation.setAccept(false);
        }
        taskRelDao.insert(taskRelation);

        //todo: return new task_id combining logs array
        TaskCreateVO taskCreateVO = new TaskCreateVO();
        taskCreateVO.setTask_id(new_task.getTask_id());
        taskCreateVO.setLogs(logs);
        return taskCreateVO;
    }

    @Override
//    @Transactional
    public List<Log> editTask(String token, String task_id, TaskInfo_ReqVO taskInfo_reqVO) {
        String user_id = jwtUtil.tryGetId(token);
        //todo: find old task info and update, mongodb
        TaskInfo taskInfo = taskInfoDao.findById(task_id).get();
        TaskInfo taskInfo_forComp = taskInfoDao.findById(task_id).get();
        taskInfo.updateInfo(taskInfo_reqVO);
        String changes = TaskComparator.getDiff(taskInfo_forComp, taskInfo);
        if (changes == "" ) return taskInfo.getLogs();
        taskInfoDao.save(taskInfo);

        //todo: update assignee, mysql
        LambdaUpdateWrapper<TaskRelation> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(TaskRelation::getAssignee_id, taskInfo_reqVO.getAssignee_id())
                .eq(TaskRelation::getTask_id, task_id);
        taskRelDao.update(null, updateWrapper);
        //todo: update log
        String log_content = user_id + changes;
        List<Log> logs = writeLog(task_id, log_content);
        return logs;
    }

    @Override
    public List<TaskRelDTO> getTaskId(String assigner_id, String assignee_id) {
        MPJLambdaWrapper<TaskRelation> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(TaskRelation::getTask_id)
                .select(TaskRelation::getAssigner_id)
                .select(TaskRelation::getAssignee_id)
                .select(TaskRelation::getAccept);
        if (assigner_id != null){
            wrapper.eq(TaskRelation::getAssigner_id, assigner_id);
        }
        if (assignee_id != null){
            wrapper.eq(TaskRelation::getAssignee_id, assignee_id);
        }
        if (!StringUtils.equals(assigner_id, assignee_id)){
            wrapper.ne(TaskRelation::getAssigner_id, TaskRelation::getAssignee_id);
        }
        return taskRelDao.selectJoinList(TaskRelDTO.class, wrapper);
    }

    /**
     *
     * @param token this can be token or already a user_id
     * @param toWho
     * @param other_id
     * @return
     */
    @Override
    public List<TaskInfo_ResVO> getTask(String token, Integer toWho, String other_id) {
        String token_id = null;
        try {
            //try to convert token, if not a token, it must be a user_id
            token = token.replace("Bearer ", "");
            token_id = jwtUtil.userIdInToken(token);
        } catch (Exception e) {
            token_id = token;
        }
        //todo: get all task id from mysql
        List<TaskRelDTO> taskRelDTOS = null;
        if (toWho == 0){
            taskRelDTOS = getTaskId(token_id, other_id);
        }else {
            taskRelDTOS = getTaskId(other_id, token_id);
        }
        //todo: find all task info from ids
        List<TaskInfo_ResVO> taskInfo_resVOS = new ArrayList<>();
        Optional<TaskInfo> taskInfo = null;
        for (TaskRelDTO taskRelDTO : taskRelDTOS){
            taskInfo = taskInfoDao.findById(taskRelDTO.getTask_id());
            if (taskInfo.isPresent()){
                taskInfo_resVOS.add(new TaskInfo_ResVO(taskInfo.get(), taskRelDTO));
            }

        }
        return taskInfo_resVOS;
    }

    @Override
    public List<TaskInfo_ResVO> getTaskToOther(String token) {
        return getTask(token, 0, null);
    }

    @Override
    public List<TaskInfo_ResVO> getTaskToOtherComplete(String token) {
        return searchTaskOr(token, new String[]{"task_status"}, "completed", new int[]{0});
    }

    @Override
    public List<TaskInfo_ResVO> getTaskToMe(String token) {
        return getTask(token, 1, null);
    }

    @Override
    public List<TaskInfo_ResVO> getTaskToMyself(String token) {
        String token1 = token.replace("Bearer ", "");
        String token_id = jwtUtil.userIdInToken(token1);
        return getTask(token, 1, token_id);
    }

    @Override
    public int acceptTask(String token, String task_id) {
        String user_id = jwtUtil.tryGetId(token);

        LambdaUpdateWrapper<TaskRelation> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(TaskRelation::getAccept, true)
                .eq(TaskRelation::getTask_id, task_id);

        //set task state and start date if necessary
        Optional<TaskInfo> optional = taskInfoDao.findById(task_id);
        if (optional.isPresent()){
            TaskInfo taskInfo = optional.get();
            String start_date = taskInfo.getStart_date();
            if (!DateUtil.isFuture(start_date)){
                //edit start date to now and status into in progress
                Query query = new Query(Criteria.where("task_id").is(task_id));
                Update update = new Update().set("task_status", "in progress")
                                            .set("start_date", DateUtil.createNewDate());
                if (mongoTemplate.updateFirst(query, update, TaskInfo.class).getModifiedCount() == 0) {
                    return 0;
                }
            }
        }
        //todo: update log
        String log_content = user_id + " accept task";
        writeLog(task_id, log_content);
        return taskRelDao.update(null, updateWrapper);
    }

    @Override
//    @Transactional
    public int declineTask(String token, String task_id) {
        String user_id = jwtUtil.tryGetId(token);

        //todo: find old relation
        QueryWrapper<TaskRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", task_id);
        TaskRelation taskRelation = taskRelDao.selectOne(queryWrapper);
        //todo: update assignee from assigner
        LambdaUpdateWrapper<TaskRelation> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(TaskRelation::getAccept, 1)
                    .set(TaskRelation::getAssignee_id, taskRelation.getAssigner_id())
                    .eq(TaskRelation::getTask_id, task_id);

        //todo: update log
        String log_content = user_id + " decline task";
        writeLog(task_id, log_content);
        return taskRelDao.update(null, updateWrapper);
    }

    @Override
    public boolean setStatus(String token, EditStatusVO editStatusVO) {
        String user_id = jwtUtil.tryGetId(token);
        Query query = new Query(Criteria.where("task_id").is(editStatusVO.getTask_id()));
        Update update = new Update().set("task_status", editStatusVO.getStatus());
        //todo: update log
        String log_content = user_id + " set status to: " + editStatusVO.getStatus();
        writeLog(editStatusVO.getTask_id(), log_content);
        if (mongoTemplate.updateFirst(query, update, TaskInfo.class).getModifiedCount() > 0) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean setFeedback(String token, SetFeedbackVO setFeedbackVO) {
        String user_id = jwtUtil.tryGetId(token);
        Query query = new Query(Criteria.where("task_id").is(setFeedbackVO.getTask_id()));
        Update update = new Update().set("task_score", setFeedbackVO.getTask_score())
                                    .set("task_feedback", setFeedbackVO.getTask_feedback());
        if (mongoTemplate.updateFirst(query, update, TaskInfo.class).getModifiedCount() > 0 &&
            setStatus(token, new EditStatusVO(setFeedbackVO.getTask_id(),"completed"))) {
            //todo: update log
            String log_content = user_id + " set feedback";
            writeLog(setFeedbackVO.getTask_id(), log_content);
            return true;
        }else {
            return false;
        }
    }

    /**
     *
     * @param token
     * @param fields
     * @param target
     * @param toWho 0: to others
     *              1: others to me
     *              2: to myself
     * @return
     */
    @Override
    public List<TaskInfo_ResVO> searchTaskOr(String token, String[] fields, Object target, int[] toWho) {
        String token_id = null;
        try {
            token = token.replace("Bearer ", "");
            token_id = jwtUtil.userIdInToken(token);
        } catch (Exception e) {
            token_id = token;
        }
        //todo: get all task id into set (according to token user id)
//        Set<String> task_ids = new HashSet<>();

        //get tasks:0.assign to other 1.other assign to me 2.assign to myself
        List<TaskRelDTO> taskRelDTOS = new ArrayList<>();
        for (int i : toWho){
            if (i == 0) taskRelDTOS.addAll(getTaskId(token_id, null));
            if (i == 1) taskRelDTOS.addAll(getTaskId(null, token_id));
            if (i == 2) taskRelDTOS.addAll(getTaskId(token_id, token_id));
        }

        List<TaskInfo_ResVO> res = new ArrayList<>();
        TaskInfo taskInfo = null;
        List<Criteria> criteria_field = new ArrayList<>();
        for (String field : fields){
            if (field.equals("task_desc") || field.equals("task_name")){
                String regex = ".*" + (String)target + ".*";
                criteria_field.add(Criteria.where(field).regex(regex));
            }else {
                criteria_field.add(Criteria.where(field).is(target));
            }
        }
        for (TaskRelDTO taskRelDTO : taskRelDTOS){
            //todo: add query criteria for mongodb
            Criteria criteria = new Criteria();
            if (fields != null){
                criteria.orOperator(criteria_field.toArray(new Criteria[criteria_field.size()]));
            }
//            criteria.andOperator(Criteria.where("task_id").is(taskRelDTO.getTask_id()));
            criteria.and("task_id").is(taskRelDTO.getTask_id());
            Query query = new Query(criteria);
            taskInfo = mongoTemplate.findOne(query, TaskInfo.class, "taskInfo");
            //todo: auto update status
            if (taskInfo != null) {
                autoUpdateStatus(taskInfo, taskRelDTO);
                res.add(new TaskInfo_ResVO(taskInfo, taskRelDTO));
            }
        }
        return res;
    }

    @Override
    public List<TaskInfo_ResVO> searchTaskAnd(String token, TaskSearchVO searchVO, int[] toWho) {
        String token_id = null;
        try {
            token = token.replace("Bearer ", "");
            token_id = jwtUtil.userIdInToken(token);
        } catch (Exception e) {
            token_id = token;
        }

        List<TaskRelDTO> taskRelDTOS = new ArrayList<>();
        for (int i : toWho){
            if (i == 0) taskRelDTOS.addAll(getTaskId(token_id, null));
            if (i == 1) taskRelDTOS.addAll(getTaskId(null, token_id));
            if (i == 2) taskRelDTOS.addAll(getTaskId(token_id, token_id));
        }
        List<TaskInfo_ResVO> res = new ArrayList<>();
        TaskInfo taskInfo = null;
        List<Criteria> criteria_field = new ArrayList<>();

        String task_id;
        String task_name;
        String task_desc;
        String end_date;
        String task_status;
        String task_tag;
        if ((task_id = searchVO.getTask_id()) != null){
            criteria_field.add(Criteria.where("task_id").is(task_id));
        }
        if ((task_name = searchVO.getTask_name()) != null){
            String regex = ".*" + (String)task_name + ".*";
            criteria_field.add(Criteria.where("task_name").regex(regex));
        }
        if ((task_desc = searchVO.getTask_desc()) != null){
            String regex = ".*" + (String)task_desc + ".*";
            criteria_field.add(Criteria.where("task_desc").regex(regex));
        }
        if ((end_date = searchVO.getEnd_date()) != null){
            criteria_field.add(Criteria.where("end_date").is(end_date));
        }
        if ((task_status = searchVO.getTask_status()) != null){
            criteria_field.add(Criteria.where("task_status").is(task_status));
        }
        if ((task_tag = searchVO.getTask_tag()) != null){
            criteria_field.add(Criteria.where("task_tag").is(task_tag));
        }

        for (TaskRelDTO taskRelDTO : taskRelDTOS){
            //todo: add query criteria for mongodb
            Criteria criteria = new Criteria();
            if (criteria_field.size() > 0){
                criteria.andOperator(criteria_field.toArray(new Criteria[criteria_field.size()]));
            }
//            criteria.andOperator(Criteria.where("task_id").is(taskRelDTO.getTask_id()));
            criteria.and("task_id").is(taskRelDTO.getTask_id());
            Query query = new Query(criteria);
            taskInfo = mongoTemplate.findOne(query, TaskInfo.class, "taskInfo");
            //todo: auto update status
            if (taskInfo != null) {
                autoUpdateStatus(taskInfo, taskRelDTO);
                res.add(new TaskInfo_ResVO(taskInfo, taskRelDTO));
            }
        }
        return res;
    }

    @Override
    public TaskInfo_ResVO getTaskById(String token, String task_id) {
        List<TaskInfo_ResVO> res = searchTaskOr(token, new String[]{"task_id"}, task_id, new int[]{0,1,2});
        return res.size() > 0 ? res.get(0) : null;
    }

    /**
     * this method is lazy update for accept task with future start date
     * trigger when status == not start, accept == true
     * @param taskInfo
     * @param taskRelDTO
     * @return
     */
    @Override
    public boolean autoUpdateStatus(TaskInfo taskInfo, TaskRelDTO taskRelDTO) {
        String start_date = taskInfo.getStart_date();
        boolean accept = taskRelDTO.getAccept();
        String task_status = taskInfo.getTask_status();
        if (accept && task_status.equals("not started") && !DateUtil.isFuture(start_date)){
            Query query = new Query(Criteria.where("task_id").is(taskInfo.getTask_id()));
            Update update = new Update().set("task_status", "in progress");
            //todo: modify the vo, too
            taskInfo.setTask_status("in progress");
            if (mongoTemplate.updateFirst(query, update, TaskInfo.class).getModifiedCount() > 0) {
                return true;
            }else {
                return false;
            }
        }
        return true;
    }


    @Override
    public List<Log> writeLog(String task_id, String log) {
        //todo: get old logs
        TaskInfo taskInfo;
        Query query = new Query(Criteria.where("task_id").is(task_id));
        taskInfo = mongoTemplate.findOne(query, TaskInfo.class);
        if (taskInfo == null) return null;
        List<Log> logs = taskInfo.getLogs();
        Log new_log = new Log();
//        new_log.setTimestamp(DateUtil.createNewTimeStamp());
        new_log.setContent(DateUtil.createNewTimeStamp() + " -- " + log);
        //todo: update logs
        logs.add(new_log);
        taskInfo.setLogs(logs);
        taskInfoDao.save(taskInfo);
        return logs;
    }
}
