/** schema 생성 */
create schema cucumber;

/* sns sns **/
create table sns(
    sns_id int(11) not null comment 'sns id' auto_increment primary key,
    sns_name varchar(100) comment 'sns 이름'
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='sns';

insert into cucumber.sns(sns_name)
values('google');

/* region 지역 **/
create table region(
    region_id int(11) not null comment '지역 id' auto_increment primary key,
    region_name varchar(100) comment '시도',
    si varchar(100) comment '시군구',
    dong varchar(100) comment '읍면동',
    li varchar(100) comment '리',
    lati_tude decimal(16,14) comment '위도',
    longi_tude decimal(16,14) comment '경도'
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='지역';

/* 회원 member **/
create table member(
    member_id int(11) not null comment '멤버 id' auto_increment primary key,
    sns_id int(11) not null comment 'sns id',
    sns_value varchar(100) default null comment 'sns 값',
    name varchar(100) comment '이름',
    email varchar(100) comment '이메일',
    region_id int(11) comment '지역 id',
    manners_temperature decimal(4,1) default 36.5 comment '매너온도',
    foreign key (sns_id) references sns(sns_id) on update cascade,
    foreign key (region_id) references region(region_id) on update cascade
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='회원';

/** 상품 상태 item_status */
create table item_status(
    item_status_id int(11) comment '상품 상태 id' auto_increment primary key ,
    item_status_name varchar(100) comment '상품 상태명'
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='상품 상태';

insert into cucumber.item_status(item_status_name)
values('판매중');

/** 상품 카테고리 category */
create table category(
    category_id int(11) comment '상품 카테고리 id' auto_increment primary key,
    category_name varchar(50) not null comment '상품 카테고리 이름'
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='상품 카테고리';

/** 상품 item */
create table item(
    item_id int(11) not null comment '상품 id' auto_increment primary key,
    member_id int(11) not null comment '멤버 id',
    item_name varchar(100) comment '상품 이름',
    item_info varchar(100) comment '상품 설명',
    post_date datetime comment '게시 일자',
    update_date datetime comment '수정 일자',
    donation_flag tinyint(1) not null comment '나눔 여부',
    price int(11) comment '가격',
    category_id int(11) comment '상품 카테고리 id',
    price_negotiation_yn tinyint(1) default 0 comment '가격 제안가능 여부',
    item_status_id int(11) default 1 comment '상태 id',
    view_count int(11) comment '조회 수',
    foreign key (member_id) references member(member_id) on delete cascade,
    foreign key (category_id) references category(category_id) on update cascade,
    foreign key (item_status_id) references item_status(item_status_id) on update cascade
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='상품';

/** 관심 favorite */
create table favorite(
    item_id int(11) comment '상품 id',
    member_id int(11) comment '멤버 id',
    foreign key (item_id) references item(item_id) on delete cascade,
    foreign key (member_id) references member(member_id) on delete cascade,
    primary key (item_id, member_id)
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='관심';

/** 판매자 후기 seller_review */
create table seller_review(
    item_id int(11) comment '상품 id' primary key,
    review varchar(100) comment '후기',
    foreign key (item_id) references item(item_id) on delete cascade
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='판매자 후기';

/** 구매자 후기 buyer_review */
create table buyer_review(
    item_id int(11) comment '상품 id',
    member_id int(11) comment '멤버 id',
    review varchar(100) comment '후기',
    foreign key (item_id) references item(item_id) on delete cascade,
    foreign key (member_id) references member(member_id) on delete cascade,
    primary key (item_id, member_id)
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='구매자 후기';

/** 구매 신청 purchase_request */
create table purchase_request(
    item_id int(11) not null comment '상품 id',
    member_id int(11) not null comment '멤버 id',
    foreign key (item_id) references item(item_id) on delete cascade,
    foreign key (member_id) references member(member_id) on delete cascade,
    primary key (item_id, member_id)
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='구매 신청';

/** 상품 이미지 item_image */
create table item_image(
    image_id int(11) comment '이미지 id' auto_increment primary key,
    original_name varchar(1000) comment '기존 이미지명',
    key_name varchar(1000) comment '저장된 이미지명',
    item_id int(11) not null comment '상품 id',
    foreign key (item_id) references item(item_id) on delete cascade
)engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci comment='이미지';