# Task Hero

Task Hero is a team management tool designed to streamline resource allocation for projects within teams. This efficient solution empowers team managers to assign tasks to members, taking into account their individual workloads, while also enabling them to monitor the project's progress. Notably, Task Hero boasts a unique recommending feature, which suggests the best team member pairings for collaborative work, enhancing overall productivity and synergy among team members.

## Table of Contents

- [Task Hero](#task-hero)
  - [Table of Contents](#table-of-contents)
  - [Contributors](#contributors)
  - [Live Demo](#live-demo)
  - [Features](#features)
  - [Local Deployment](#local-deployment)
  - [Frontend](#frontend)
  - [System Architecture](#system-architecture)
  - [File Structure](#file-structure)
  - [Technical Support or Questions](#technical-support-or-questions)

## Contributors

The backend of Task Hero was developed by

1. Joseph Zhou
2. Yue Wu
3. Wenzhe Huang
4. Shukai Luo
5. Dongzhe Chen

## Live Demo

## Features

1. User can sign up with their name, email and password.
2. User can maintain a profile with their basic information (mobile number, email, bio, specialisation, etc.).
3. User can find other users by searching their name, email or id and establish connection with them through their profiles.
4. User can view assigned tasks of the connected users they have in common on their profile pages.
5. Users can efficiently manage both their personal tasks and the tasks they have assigned to others on the Dashboard.
6. Users have the ability to create tasks by providing specific details, including the task label, assignee, priority level, stsart date, end date, wordload and included items. Once the task is created, they can then wait for others to accept the task.
7. Tasks have 4 different statuses, "Not Started", "In Progress", "Blocked" and "Completed".
8. For every task, it has a log books to automatically record modification that has been made.
9. Assigner can give feedback to assignee once they have completed the task.
10. Over time, the number of tasks existing in the system will build up, so the users can search, through all tasks assigned to themselves or any other users they are connected to, by any combination of id, name, description and/or deadline, and view the full details.
11. Users can view the current workload of their connected colleagues. This workload is estimated by the system based on a combination of assigned tasks, task states, task deadlines, and individual task workloads.
12. Over time, the system can continuously recommend potential cooperative partners for users to work with, based on the tasks they have taken to complete in the past.

## Local Deployment
[Please view this local deployment instruction.](ready-to-deploy/instruction.md)

## Frontend

Click to view [the repo of the frontend](https://github.com/happicodie/Task-Hero-Frontend)

## System Architecture
<img alt="image" src="public/Architecture.png">

## File Structure

```
.
├── LICENSE
├── README.md
├── backend.iml
├── mvnw
├── mvnw.cmd
├── pom.xml
├── ready-to-deploy
│   ├── data.sql
│   ├── instruction.md
│   └── task_hero.jar
└──src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── fivesigma
    │   │           └── backend
    │   │               ├── BackendApplication.java
    │   │               ├── config
    │   │               │   ├── CorsConfig.java
    │   │               │   ├── MongodbConfig.java
    │   │               │   ├── MyBatisConfig.java
    │   │               │   ├── SecurityConfig.java
    │   │               │   └── SwaggerConfig.java
    │   │               ├── controller
    │   │               │   ├── RecommendController.java
    │   │               │   ├── SearchController.java
    │   │               │   ├── TaskController.java
    │   │               │   ├── UserAuthController.java
    │   │               │   ├── UserConnectionController.java
    │   │               │   ├── UserInfoController.java
    │   │               │   └── UserWorkloadController.java
    │   │               ├── dao
    │   │               │   ├── IConnectionDao.java
    │   │               │   ├── ITagNameDao.java
    │   │               │   ├── ITaskInfoDao.java
    │   │               │   ├── ITaskRelDao.java
    │   │               │   ├── ITokenDAO.java
    │   │               │   ├── IUserAuthDao.java
    │   │               │   ├── IUserImageDao.java
    │   │               │   ├── IUserInfoDao.java
    │   │               │   ├── IUserTagDao.java
    │   │               │   ├── IUserWorkloadDao.java
    │   │               │   └── UserAuthDAO.java
    │   │               ├── dto
    │   │               │   ├── TaskRelDTO.java
    │   │               │   ├── UserAuthDTO.java
    │   │               │   ├── UserBasicInfoDTO.java
    │   │               │   ├── UserDetailDTO.java
    │   │               │   ├── UserInfoDTO.java
    │   │               │   ├── UserTagDTO.java
    │   │               │   └── UserWorkloadDTO.java
    │   │               ├── filter
    │   │               │   ├── AuthenticationFilter.java
    │   │               │   ├── JWTRequestFilter.java
    │   │               │   └── LoginFilter.java
    │   │               ├── po_entity
    │   │               │   ├── CheckList.java
    │   │               │   ├── Connection.java
    │   │               │   ├── Log.java
    │   │               │   ├── TagName.java
    │   │               │   ├── TaskInfo.java
    │   │               │   ├── TaskRelation.java
    │   │               │   ├── TokenBlackList.java
    │   │               │   ├── UserAuth.java
    │   │               │   ├── UserImage.java
    │   │               │   ├── UserInfo.java
    │   │               │   ├── UserTag.java
    │   │               │   └── UserWorkload.java
    │   │               ├── service
    │   │               │   ├── IConnectionService.java
    │   │               │   ├── IRecommendService.java
    │   │               │   ├── ISearchService.java
    │   │               │   ├── ITaskService.java
    │   │               │   ├── ITokenService.java
    │   │               │   ├── IUserInfoService.java
    │   │               │   ├── IUserTagService.java
    │   │               │   ├── IUserWorkloadService.java
    │   │               │   └── impl
    │   │               │       ├── ConnectionServiceImpl.java
    │   │               │       ├── RecommendServiceImpl.java
    │   │               │       ├── SearchServiceImpl.java
    │   │               │       ├── TaskServiceImpl.java
    │   │               │       ├── TokenServiceImpl.java
    │   │               │       ├── UserAuthServiceImpl.java
    │   │               │       ├── UserInfoServiceImpl.java
    │   │               │       ├── UserTagServiceImpl.java
    │   │               │       └── UserWorkloadServiceImpl.java
    │   │               ├── test_class
    │   │               │   └── fake_db.java
    │   │               ├── util
    │   │               │   ├── AutoFillHandler.java
    │   │               │   ├── CustomPwdEncoder.java
    │   │               │   ├── DateUtil.java
    │   │               │   ├── EmailUtil.java
    │   │               │   ├── HttpStatusInterceptor.java
    │   │               │   ├── JWTUtil.java
    │   │               │   ├── ResponseUtil.java
    │   │               │   ├── TaskComparator.java
    │   │               │   └── TokenUtil.java
    │   │               └── vo
    │   │                   ├── AvailTagsVO.java
    │   │                   ├── ConnectionVO.java
    │   │                   ├── EditProfileVO.java
    │   │                   ├── EditStatusVO.java
    │   │                   ├── InvitationVO.java
    │   │                   ├── SearchGlobalVO.java
    │   │                   ├── SetFeedbackVO.java
    │   │                   ├── TaskCreateVO.java
    │   │                   ├── TaskInfo_ReqVO.java
    │   │                   ├── TaskInfo_ResVO.java
    │   │                   ├── TaskProfileVO.java
    │   │                   ├── TaskSearchVO.java
    │   │                   ├── TaskStatusVO.java
    │   │                   ├── TokenVO.java
    │   │                   ├── UserAuthVO.java
    │   │                   ├── UserInfoVO.java
    │   │                   ├── UserRecommendVO.java
    │   │                   ├── UserRegVO.java
    │   │                   └── UserSearchVO.java
    │   └── resources
    │       ├── application.properties
    │       ├── application.yml
    │       └── db
    │           ├── connection_table.sql
    │           ├── schema_s1.sql
    │           ├── sql
    │           │   ├── connection_table.sql
    │           │   ├── tag_name.sql
    │           │   ├── task_rel_table.sql
    │           │   ├── user_auth.sql
    │           │   ├── user_image.sql
    │           │   ├── user_info.sql
    │           │   ├── user_tag.sql
    │           │   └── user_workload.sql
    │           ├── sql-insert
    │           │   ├── connection_table.sql
    │           │   ├── tag_name.sql
    │           │   ├── task_rel_table.sql
    │           │   ├── user_auth.sql
    │           │   ├── user_image.sql
    │           │   ├── user_info.sql
    │           │   ├── user_tag.sql
    │           │   └── user_workload.sql
    │           ├── tag_name.sql
    │           ├── task_rel_table.sql
    │           ├── user_auth.sql
    │           ├── user_image.sql
    │           ├── user_info.sql
    │           ├── user_tag.sql
    │           └── user_workload.sql
    └── test
        └── java
            └── com
                └── fivesigma
                    └── backend
                        ├── BackendApplicationTests.java
                        └── TestController.java
 

```

## Technical Support or Questions

If you have questions or need help running the app please contact [Joseph Zhou](josephchow.message@gmail.com).
