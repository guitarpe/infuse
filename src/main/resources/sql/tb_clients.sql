create table tb_clients
(
    ID              int auto_increment,
    NM_CLIENT       varchar(100) not null,
    EMAIL_CLIENT    varchar(100) not null,
    PHONE_CLIENT    varchar(20) not null,
    DT_REGISTER     datetime     default CURRENT_TIMESTAMP not null,
    DT_UPDATE       datetime     null,
    constraint TB_CLIENTS_ID_uindex
        unique (ID)
);