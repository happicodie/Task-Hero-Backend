package com.fivesigma.backend.util;

import com.fivesigma.backend.po_entity.CheckList;
import com.fivesigma.backend.vo.TaskInfo_ResVO;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Andy
 * @date 2022/11/11
 */

@Component
public class TaskComparator implements Comparator<TaskInfo_ResVO> {
    @Override
    public int compare(TaskInfo_ResVO o1, TaskInfo_ResVO o2) {
        Integer score1 = o1.getTask_score();
        Integer score2 = o2.getTask_score();
        return score1.compareTo(score2);
    }

    public static String getDiff(Object s1, Object s2) {
        try {
            List<String> values = new ArrayList<>();
            for (Field field : s1.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals("checkListList")){
                    List<CheckList> value1 = (List<CheckList>) field.get(s1);
                    List<CheckList> value2 = (List<CheckList>) field.get(s2);
                    String diff_check = checkListDiff(value1, value2);
                    values.add(diff_check);
                }else {
                    Object value1 = field.get(s1);
                    Object value2 = field.get(s2);
                    if (value1 != null && value2 != null) {
                        if (!Objects.equals(value1, value2)) {
                            values.add(String.valueOf("change " + field.getName()+" from: "+value1+" to: "+value2));
                        }
                    }
                }

            }
            return String.join(" || ", values);
        } catch (Exception e) {
            return null;
        }
    }

    public static String checkListDiff(List<CheckList> list_old, List<CheckList> list_new){
        List<CheckList> old_set = new ArrayList<>(list_old);
        List<CheckList> new_set = new ArrayList<>(list_new);
        //add new item
        if (new_set.size() > old_set.size()){
            String diff = listDiff(new_set, old_set);
            return "add check: " + diff;
        }else if (new_set.size() < old_set.size()){
            String diff = listDiff(old_set, new_set);
            return "delete check: " + diff;
        }else {
            StringJoiner joiner = new StringJoiner(", ");
            for (CheckList check : new_set){
                if (old_set.contains(check)){
                    Boolean new_bool = check.isBool_check();
                    int index = old_set.indexOf(check);
                    Boolean old_bool = old_set.get(index).isBool_check();
                    if (new_bool != old_bool){
                        joiner.add(" check-item: " + check.getCheck_detail() + " change status");
                    }
                }else {
                    joiner.add(" check-item: "  + check.getCheck_detail() + " modify");
                }

            }
            return joiner.toString();
        }
    }
    public static String listDiff(List<CheckList> list_long, List<CheckList> list_short){
        list_long.removeAll(list_short);
        StringJoiner joiner = new StringJoiner(", ");
        for(CheckList check : list_long){
            joiner.add(check.getCheck_detail());
        }
        return joiner.toString();
    }

}
