create table tb_clients
(
    ID          int auto_increment,
    NM_CLIENT   varchar(100) not null,
    DT_REGISTER datetime     null,
    constraint TB_CLIENTS_ID_uindex
        unique (ID)
);