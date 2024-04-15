create table tb_logs
(
    ID          int auto_increment,
    NM_SERVICE  varchar(100)                       not null,
    NM_METHOD   varchar(100)                       not null,
    LOG_EXECUTE varchar(300)                       not null,
    DT_REGISTER datetime default CURRENT_TIMESTAMP null,
    constraint TB_LOGS_ID_uindex
        unique (ID)
);