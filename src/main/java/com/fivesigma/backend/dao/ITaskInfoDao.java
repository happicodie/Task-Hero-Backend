package com.fivesigma.backend.dao;

import com.fivesigma.backend.po_entity.TaskInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskInfoDao extends MongoRepository<TaskInfo, String> {
}
