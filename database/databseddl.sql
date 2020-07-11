CREATE SCHEMA IF NOT EXISTS test_blog;


CREATE TABLE test_blog.tbl_profile (

	profile_id serial NOT NULL,
    user_name varchar(200),
    email varchar(200),
    imageUrl varchar(200),
    CONSTRAINT pk_profile PRIMARY KEY (profile_id)
);



CREATE TABLE test_blog.tbl_blog (

	blog_id serial NOT NULL,
    profile_id int4 NOT NULL,
    blog_name varchar(200),
    blog_description varchar(500),
    created_date Timestamp,
	updated_date Timestamp,
    CONSTRAINT pk_blog PRIMARY KEY (blog_id),
    CONSTRAINT fk_profile FOREIGN KEY (profile_id) REFERENCES test_blog.tbl_profile(profile_id)

);


CREATE TABLE test_blog.tbl_article (

	article_id serial NOT NULL,
	blog_id int4 NOT NULL,
	article_title varchar(200),
	article_text varchar(200),
	created_date Timestamp,
	updated_date Timestamp,
	CONSTRAINT pk_article PRIMARY KEY (article_id),
	CONSTRAINT fk_blog FOREIGN KEY (blog_id) REFERENCES test_blog.tbl_blog(blog_id)
);











