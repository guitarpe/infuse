create table tb_orders
(
    ID               int auto_increment,
    CLIENT_ID        int                                not null,
    NUM_CONTROL      int                                not null,
    NM_PRODUCT       varchar(100)                       not null,
    VL_PRODUCT       decimal  default 0                 not null,
    AMOUNT_ORDER     int      default 0                 not null,
    PERCENT_DISCOUNT float    default 0                 null,
    VL_DISCOUNT      decimal  default 0                 null,
    VL_ORDER         decimal                            null,
    DT_REGISTER      datetime default CURRENT_TIMESTAMP not null,
    constraint tb_orders_ID_uindex
        unique (ID),
    constraint tb_orders_NUM_CONTROL_uindex
        unique (NUM_CONTROL)
);

