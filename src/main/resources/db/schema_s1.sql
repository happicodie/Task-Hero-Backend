DROP TABLE IF EXISTS user_auth;
CREATE TABLE IF NOT EXISTS user_auth(
    auth_id int auto_increment,
    user_id varchar(20) NOT NULL UNIQUE ,
    user_name varchar(40) NOT NULL UNIQUE,
    user_password varchar(100) NOT NULL,
    primary key (auth_id)
    );

DROP TABLE IF EXISTS user_info;
CREATE TABLE IF NOT EXISTS user_info(
    info_id int auto_increment,
    user_id varchar(20) NOT NULL UNIQUE,
    user_p_name varchar(50),
    user_mobile varchar(20),
    user_sentence varchar(500),
    user_comp varchar(50),
    primary key (info_id)
#     foreign key (user_id) references user_auth(user_id)
    );

DROP TABLE IF EXISTS user_tag;
CREATE TABLE IF NOT EXISTS user_tag(
    u_tag_id int auto_increment,
    user_id varchar(20),
#     tag_name varchar(20),
    tag_id int,
    primary key (u_tag_id)
#     foreign key (user_id) references user_auth(user_id),
#     foreign key (tag_id) references tag_name(tag_id)
);

DROP TABLE IF EXISTS tag_name;
CREATE TABLE IF NOT EXISTS tag_name(
   n_tag_id int auto_increment,
   tag_id int,
   tag_name varchar(20),
   primary key (n_tag_id)
);

DROP TABLE IF EXISTS user_image;
CREATE TABLE IF NOT EXISTS user_image(
    image_id int auto_increment,
    user_id varchar(20),
    image mediumtext,
    primary key (image_id)
#     foreign key (user_id) references user_auth(user_id)
);


DROP TABLE IF EXISTS connection_table;
CREATE TABLE IF NOT EXISTS connection_table(
    connection_id int auto_increment,
    user_id_A varchar(20),
    user_id_B varchar(20),
    connection_result int,
    connection_msg varchar(300),
    primary key  (connection_id)
#     foreign key (user_id_A) references user_auth(user_id),
#     foreign key (user_id_B) references user_auth(user_id)
);

DROP TABLE IF EXISTS token_table;
CREATE TABLE IF NOT EXISTS token_table(
   token_id int auto_increment,
   token varchar(300),
   primary key (token_id)
);

DROP TABLE IF EXISTS task_rel_table;
CREATE TABLE IF NOT EXISTS task_rel_table(
    sys_id int auto_increment,
    task_id varchar(30) NOT NULL UNIQUE,
    assigner_id varchar(20) NOT NULL,
    assignee_id varchar(20) NOT NULL,
    accept boolean,
    primary key (sys_id)
);

DROP TABLE IF EXISTS user_workload;
CREATE TABLE IF NOT EXISTS user_workload(
    sys_id int auto_increment,
    user_id varchar(30) NOT NULL UNIQUE,
    user_available double,
    user_working double,
    user_busy double,
    primary key (sys_id)
);