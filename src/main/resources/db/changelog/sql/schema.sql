--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: admins; Type: TABLE; Schema: public
--

CREATE TABLE public.admins (
    id bigint NOT NULL,
    admin_login character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    password character varying(255) NOT NULL
);




--
-- Name: admins_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.admins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: admins_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.admins_id_seq OWNED BY public.admins.id;


--
-- Name: block_history; Type: TABLE; Schema: public
--

CREATE TABLE public.block_history (
    id bigint NOT NULL,
    comment_text character varying(255),
    "time" timestamp(6) without time zone,
    author_id bigint,
    comment_id bigint,
    post_id bigint
);




--
-- Name: block_history_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.block_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: block_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.block_history_id_seq OWNED BY public.block_history.id;


--
-- Name: captcha; Type: TABLE; Schema: public
--

CREATE TABLE public.captcha (
    id bigint NOT NULL,
    code character varying(255),
    secret_code character varying(255),
    "time" character varying(255)
);




--
-- Name: captcha_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.captcha_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: captcha_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.captcha_id_seq OWNED BY public.captcha.id;


--
-- Name: cities; Type: TABLE; Schema: public
--

CREATE TABLE public.cities (
    id bigint NOT NULL,
    code2 character varying(255),
    international_name character varying(255),
    lat numeric,
    lng numeric,
    name character varying(255),
    open_weather_id bigint,
    state character varying(255),
    country_id bigint,
    external_id bigint
);




--
-- Name: cities_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.cities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: cities_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.cities_id_seq OWNED BY public.cities.id;


--
-- Name: countries; Type: TABLE; Schema: public
--

CREATE TABLE public.countries (
    id bigint NOT NULL,
    code2 character varying(255),
    full_name character varying(255),
    international_name character varying(255),
    name character varying(255),
    external_id bigint
);




--
-- Name: countries_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.countries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: countries_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.countries_id_seq OWNED BY public.countries.id;


--
-- Name: currencies; Type: TABLE; Schema: public
--

CREATE TABLE public.currencies (
    id bigint NOT NULL,
    name character varying(255),
    price character varying(255),
    update_time character varying(255)
);




--
-- Name: currencies_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.currencies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: currencies_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.currencies_id_seq OWNED BY public.currencies.id;




--
-- Name: dialogs; Type: TABLE; Schema: public
--

CREATE TABLE public.dialogs (
    id bigint NOT NULL,
    last_active_time timestamp(6) without time zone,
    last_message_id bigint,
    first_person_id bigint,
    second_person_id bigint
);




--
-- Name: dialogs_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.dialogs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: dialogs_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.dialogs_id_seq OWNED BY public.dialogs.id;


--
-- Name: friendships; Type: TABLE; Schema: public
--

CREATE TABLE public.friendships (
    id bigint NOT NULL,
    sent_time timestamp(6) without time zone NOT NULL,
    status_name character varying(255) NOT NULL,
    dst_person_id bigint,
    src_person_id bigint
);




--
-- Name: friendships_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.friendships_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: friendships_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.friendships_id_seq OWNED BY public.friendships.id;


--
-- Name: likes; Type: TABLE; Schema: public
--

CREATE TABLE public.likes (
    id bigint NOT NULL,
    entity_id bigint NOT NULL,
    "time" timestamp(6) without time zone NOT NULL,
    type character varying(255) NOT NULL,
    person_id bigint
);




--
-- Name: likes_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.likes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: likes_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.likes_id_seq OWNED BY public.likes.id;


--
-- Name: messages; Type: TABLE; Schema: public
--

CREATE TABLE public.messages (
    id bigint NOT NULL,
    is_deleted boolean,
    message_text text,
    read_status character varying(255),
    "time" timestamp(6) without time zone,
    author_id bigint,
    dialog_id bigint,
    recipient_id bigint
);




--
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.messages_id_seq OWNED BY public.messages.id;


--
-- Name: notifications; Type: TABLE; Schema: public
--

CREATE TABLE public.notifications (
    id bigint NOT NULL,
    contact character varying(255),
    entity_id bigint,
    is_read boolean,
    notification_type character varying(255),
    sent_time timestamp(6) without time zone,
    person_id bigint,
    sender_id bigint
);




--
-- Name: notifications_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.notifications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: notifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.notifications_id_seq OWNED BY public.notifications.id;


--
-- Name: person_settings; Type: TABLE; Schema: public
--

CREATE TABLE public.person_settings (
    id bigint NOT NULL,
    comment_comment boolean,
    friend_birthday boolean,
    friend_request boolean,
    message boolean,
    post boolean,
    post_comment boolean,
    post_like boolean
);




--
-- Name: person_settings_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.person_settings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: person_settings_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.person_settings_id_seq OWNED BY public.person_settings.id;


--
-- Name: persons; Type: TABLE; Schema: public
--

CREATE TABLE public.persons (
    id bigint NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    e_mail character varying(255),
    phone character varying(255),
    birth_date timestamp(6) without time zone,
    about character varying(255),
    change_password_token character varying(255),
    city character varying(255),
    configuration_code integer,
    country character varying(255),
    is_approved boolean,
    is_blocked boolean,
    is_deleted boolean,
    deleted_time timestamp(6) without time zone,
    last_online_time timestamp(6) without time zone,
    message_permissions character varying(255) NOT NULL,
    notifications_session_id character varying(255),
    online_status boolean NOT NULL,
    password character varying(255),
    photo character varying(255),
    reg_date timestamp(6) without time zone,
    telegram_id bigint,
    person_settings_id bigint
);




--
-- Name: persons_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.persons_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: persons_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.persons_id_seq OWNED BY public.persons.id;


--
-- Name: post2tag; Type: TABLE; Schema: public
--

CREATE TABLE public.post2tag (
    id bigint NOT NULL,
    post_id bigint,
    tag_id bigint
);




--
-- Name: post2tag_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.post2tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: post2tag_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.post2tag_id_seq OWNED BY public.post2tag.id;


--
-- Name: post_comments; Type: TABLE; Schema: public
--

CREATE TABLE public.post_comments (
    id bigint NOT NULL,
    comment_text text NOT NULL,
    is_blocked boolean NOT NULL,
    is_deleted boolean NOT NULL,
    "time" timestamp(6) without time zone NOT NULL,
    author_id bigint,
    post_id bigint,
    parent_id bigint
);




--
-- Name: post_comments_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.post_comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: post_comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.post_comments_id_seq OWNED BY public.post_comments.id;


--
-- Name: post_files; Type: TABLE; Schema: public
--

CREATE TABLE public.post_files (
    id bigint NOT NULL,
    name character varying(255),
    path character varying(255),
    post_id bigint
);




--
-- Name: post_files_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.post_files_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: post_files_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.post_files_id_seq OWNED BY public.post_files.id;


--
-- Name: posts; Type: TABLE; Schema: public
--

CREATE TABLE public.posts (
    id bigint NOT NULL,
    title character varying(255) NOT NULL,
    post_text text NOT NULL,
    "time" timestamp(6) without time zone NOT NULL,
    is_blocked boolean NOT NULL,
    is_deleted boolean NOT NULL,
    time_delete timestamp(6) without time zone,
    author_id bigint
);




--
-- Name: posts_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.posts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: posts_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.posts_id_seq OWNED BY public.posts.id;


--
-- Name: storage; Type: TABLE; Schema: public
--

CREATE TABLE public.storage (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    file_name character varying(255),
    file_size bigint,
    file_type character varying(255),
    owner_id bigint
);




--
-- Name: storage_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.storage_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: storage_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.storage_id_seq OWNED BY public.storage.id;


--
-- Name: tags; Type: TABLE; Schema: public
--

CREATE TABLE public.tags (
    id bigint NOT NULL,
    tag character varying(255) NOT NULL
);




--
-- Name: tags_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.tags_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: tags_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.tags_id_seq OWNED BY public.tags.id;


--
-- Name: weather; Type: TABLE; Schema: public
--

CREATE TABLE public.weather (
    id bigint NOT NULL,
    city character varying(255) NOT NULL,
    clouds character varying(255),
    date timestamp(6) without time zone NOT NULL,
    open_weather_ids character varying,
    temp double precision
);




--
-- Name: weather_id_seq; Type: SEQUENCE; Schema: public
--

CREATE SEQUENCE public.weather_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




--
-- Name: weather_id_seq; Type: SEQUENCE OWNED BY; Schema: public
--

ALTER SEQUENCE public.weather_id_seq OWNED BY public.weather.id;


--
-- Name: admins id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.admins ALTER COLUMN id SET DEFAULT nextval('public.admins_id_seq'::regclass);


--
-- Name: block_history id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.block_history ALTER COLUMN id SET DEFAULT nextval('public.block_history_id_seq'::regclass);


--
-- Name: captcha id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.captcha ALTER COLUMN id SET DEFAULT nextval('public.captcha_id_seq'::regclass);


--
-- Name: cities id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.cities ALTER COLUMN id SET DEFAULT nextval('public.cities_id_seq'::regclass);


--
-- Name: countries id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.countries ALTER COLUMN id SET DEFAULT nextval('public.countries_id_seq'::regclass);


--
-- Name: currencies id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.currencies ALTER COLUMN id SET DEFAULT nextval('public.currencies_id_seq'::regclass);


--
-- Name: dialogs id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.dialogs ALTER COLUMN id SET DEFAULT nextval('public.dialogs_id_seq'::regclass);


--
-- Name: friendships id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.friendships ALTER COLUMN id SET DEFAULT nextval('public.friendships_id_seq'::regclass);


--
-- Name: likes id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.likes ALTER COLUMN id SET DEFAULT nextval('public.likes_id_seq'::regclass);


--
-- Name: messages id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.messages ALTER COLUMN id SET DEFAULT nextval('public.messages_id_seq'::regclass);


--
-- Name: notifications id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.notifications ALTER COLUMN id SET DEFAULT nextval('public.notifications_id_seq'::regclass);


--
-- Name: person_settings id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.person_settings ALTER COLUMN id SET DEFAULT nextval('public.person_settings_id_seq'::regclass);


--
-- Name: persons id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.persons ALTER COLUMN id SET DEFAULT nextval('public.persons_id_seq'::regclass);


--
-- Name: post2tag id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.post2tag ALTER COLUMN id SET DEFAULT nextval('public.post2tag_id_seq'::regclass);


--
-- Name: post_comments id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.post_comments ALTER COLUMN id SET DEFAULT nextval('public.post_comments_id_seq'::regclass);


--
-- Name: post_files id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.post_files ALTER COLUMN id SET DEFAULT nextval('public.post_files_id_seq'::regclass);


--
-- Name: posts id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.posts ALTER COLUMN id SET DEFAULT nextval('public.posts_id_seq'::regclass);


--
-- Name: storage id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.storage ALTER COLUMN id SET DEFAULT nextval('public.storage_id_seq'::regclass);


--
-- Name: tags id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.tags ALTER COLUMN id SET DEFAULT nextval('public.tags_id_seq'::regclass);


--
-- Name: weather id; Type: DEFAULT; Schema: public
--

ALTER TABLE ONLY public.weather ALTER COLUMN id SET DEFAULT nextval('public.weather_id_seq'::regclass);


--
-- Name: admins admins_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT admins_pkey PRIMARY KEY (id);


--
-- Name: block_history block_history_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.block_history
    ADD CONSTRAINT block_history_pkey PRIMARY KEY (id);


--
-- Name: captcha captcha_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.captcha
    ADD CONSTRAINT captcha_pkey PRIMARY KEY (id);


--
-- Name: cities cities_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id);


--
-- Name: countries countries_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.countries
    ADD CONSTRAINT countries_pkey PRIMARY KEY (id);


--
-- Name: currencies currencies_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.currencies
    ADD CONSTRAINT currencies_pkey PRIMARY KEY (id);


--
-- Name: dialogs dialogs_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.dialogs
    ADD CONSTRAINT dialogs_pkey PRIMARY KEY (id);


--
-- Name: friendships friendships_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT friendships_pkey PRIMARY KEY (id);


--
-- Name: friendships friendships_src_person_id_dst_person_id_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT friendships_src_person_id_dst_person_id_key UNIQUE (src_person_id, dst_person_id);


--
-- Name: likes likes_person_id_type_entity_id_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT likes_person_id_type_entity_id_key UNIQUE (person_id, type, entity_id);


--
-- Name: likes likes_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT likes_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- Name: person_settings person_settings_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.person_settings
    ADD CONSTRAINT person_settings_pkey PRIMARY KEY (id);


--
-- Name: persons persons_e_mail_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.persons
    ADD CONSTRAINT persons_e_mail_key UNIQUE (e_mail);


--
-- Name: persons persons_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.persons
    ADD CONSTRAINT persons_pkey PRIMARY KEY (id);


--
-- Name: post2tag post2tag_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.post2tag
    ADD CONSTRAINT post2tag_pkey PRIMARY KEY (id);


--
-- Name: post_comments post_comments_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.post_comments
    ADD CONSTRAINT post_comments_pkey PRIMARY KEY (id);


--
-- Name: post_files post_files_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.post_files
    ADD CONSTRAINT post_files_pkey PRIMARY KEY (id);


--
-- Name: posts posts_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_pkey PRIMARY KEY (id);


--
-- Name: storage storage_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.storage
    ADD CONSTRAINT storage_pkey PRIMARY KEY (id);


--
-- Name: tags tags_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- Name: tags tags_tag_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_tag_key UNIQUE (tag);


--
-- Name: weather weather_city_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.weather
    ADD CONSTRAINT weather_city_key UNIQUE (city);


--
-- Name: weather weather_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.weather
    ADD CONSTRAINT weather_pkey PRIMARY KEY (id);


--
-- Name: captcha_time_idx; Type: INDEX; Schema: public
--

CREATE UNIQUE INDEX captcha_time_idx ON public.captcha USING btree ("time" DESC);


--
-- Name: ciies_name_idx; Type: INDEX; Schema: public
--

CREATE INDEX ciies_name_idx ON public.cities USING hash (name);


--
-- Name: dialog_first_person_idx; Type: INDEX; Schema: public
--

CREATE INDEX dialog_first_person_idx ON public.dialogs USING hash (first_person_id);


--
-- Name: dialog_second_person_idx; Type: INDEX; Schema: public
--

CREATE INDEX dialog_second_person_idx ON public.dialogs USING hash (second_person_id);


--
-- Name: friendships_dst_person_idx; Type: INDEX; Schema: public
--

CREATE INDEX friendships_dst_person_idx ON public.friendships USING hash (dst_person_id);


--
-- Name: friendships_src_person_idx; Type: INDEX; Schema: public
--

CREATE INDEX friendships_src_person_idx ON public.friendships USING hash (src_person_id);


--
-- Name: friendships_status_name; Type: INDEX; Schema: public
--

CREATE INDEX friendships_status_name ON public.friendships USING hash (status_name);


--
-- Name: likes_entity_idx; Type: INDEX; Schema: public
--

CREATE INDEX likes_entity_idx ON public.likes USING hash (entity_id);


--
-- Name: likes_person_idx; Type: INDEX; Schema: public
--

CREATE INDEX likes_person_idx ON public.likes USING hash (person_id);


--
-- Name: likes_type_idx; Type: INDEX; Schema: public
--

CREATE INDEX likes_type_idx ON public.likes USING hash (type);


--
-- Name: messages_author_idx; Type: INDEX; Schema: public
--

CREATE INDEX messages_author_idx ON public.messages USING hash (author_id);


--
-- Name: messages_dialog_idx; Type: INDEX; Schema: public
--

CREATE INDEX messages_dialog_idx ON public.messages USING hash (dialog_id);


--
-- Name: messages_read_status_idx; Type: INDEX; Schema: public
--

CREATE INDEX messages_read_status_idx ON public.messages USING hash (read_status);


--
-- Name: messages_recipient_idx; Type: INDEX; Schema: public
--

CREATE INDEX messages_recipient_idx ON public.messages USING hash (recipient_id);


--
-- Name: notifications_person_idx; Type: INDEX; Schema: public
--

CREATE INDEX notifications_person_idx ON public.notifications USING hash (person_id);


--
-- Name: persons_birth_date_idx; Type: INDEX; Schema: public
--

CREATE INDEX persons_birth_date_idx ON public.persons USING btree (birth_date);


--
-- Name: persons_city_idx; Type: INDEX; Schema: public
--

CREATE INDEX persons_city_idx ON public.persons USING btree (city);


--
-- Name: persons_country_idx; Type: INDEX; Schema: public
--

CREATE INDEX persons_country_idx ON public.persons USING btree (country);


--
-- Name: persons_first_name_idx; Type: INDEX; Schema: public
--

CREATE INDEX persons_first_name_idx ON public.persons USING btree (first_name);


--
-- Name: persons_is_deleted_idx; Type: INDEX; Schema: public
--

CREATE INDEX persons_is_deleted_idx ON public.persons USING hash (is_deleted);


--
-- Name: persons_last_name_idx; Type: INDEX; Schema: public
--

CREATE INDEX persons_last_name_idx ON public.persons USING btree (last_name);


--
-- Name: post2tag_post_idx; Type: INDEX; Schema: public
--

CREATE INDEX post2tag_post_idx ON public.post2tag USING hash (post_id);


--
-- Name: post_comments_is_deleted_idx; Type: INDEX; Schema: public
--

CREATE INDEX post_comments_is_deleted_idx ON public.post_comments USING hash (is_deleted);


--
-- Name: post_comments_post_id_idx; Type: INDEX; Schema: public
--

CREATE INDEX post_comments_post_id_idx ON public.post_comments USING hash (post_id);


--
-- Name: posts_author_idx; Type: INDEX; Schema: public
--

CREATE INDEX posts_author_idx ON public.posts USING hash (author_id);


--
-- Name: posts_is_deleted_idx; Type: INDEX; Schema: public
--

CREATE INDEX posts_is_deleted_idx ON public.posts USING hash (is_deleted);


--
-- Name: posts_post_text_idx; Type: INDEX; Schema: public
--

CREATE INDEX posts_post_text_idx ON public.posts USING btree (post_text);


--
-- Name: posts_tag_idx; Type: INDEX; Schema: public
--

CREATE INDEX posts_tag_idx ON public.tags USING hash (tag);


--
-- Name: posts_time_idx; Type: INDEX; Schema: public
--

CREATE INDEX posts_time_idx ON public.posts USING btree ("time");


--
-- Name: weather_city_idx; Type: INDEX; Schema: public
--

CREATE INDEX weather_city_idx ON public.weather USING hash (city);


--
-- Name: block_history fk_block_history_comment; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.block_history
    ADD CONSTRAINT fk_block_history_comment FOREIGN KEY (comment_id) REFERENCES public.post_comments(id);


--
-- Name: block_history fk_block_history_person; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.block_history
    ADD CONSTRAINT fk_block_history_person FOREIGN KEY (author_id) REFERENCES public.persons(id);


--
-- Name: block_history fk_block_history_post; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.block_history
    ADD CONSTRAINT fk_block_history_post FOREIGN KEY (post_id) REFERENCES public.posts(id);


--
-- Name: post_comments fk_comment_parent_id; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.post_comments
    ADD CONSTRAINT fk_comment_parent_id FOREIGN KEY (parent_id) REFERENCES public.post_comments(id);


--
-- Name: post_comments fk_comment_person; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.post_comments
    ADD CONSTRAINT fk_comment_person FOREIGN KEY (author_id) REFERENCES public.persons(id);


--
-- Name: post_comments fk_comment_post; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.post_comments
    ADD CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES public.posts(id);


--
-- Name: cities fk_country; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT fk_country FOREIGN KEY (country_id) REFERENCES public.countries(id);


--
-- Name: dialogs fk_dialog_first_person; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.dialogs
    ADD CONSTRAINT fk_dialog_first_person FOREIGN KEY (first_person_id) REFERENCES public.persons(id);


--
-- Name: dialogs fk_dialog_second_person; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.dialogs
    ADD CONSTRAINT fk_dialog_second_person FOREIGN KEY (second_person_id) REFERENCES public.persons(id);


--
-- Name: post_files fk_file_post; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.post_files
    ADD CONSTRAINT fk_file_post FOREIGN KEY (post_id) REFERENCES public.posts(id);


--
-- Name: friendships fk_friendship_person_dst; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT fk_friendship_person_dst FOREIGN KEY (dst_person_id) REFERENCES public.persons(id);


--
-- Name: friendships fk_friendship_person_src; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT fk_friendship_person_src FOREIGN KEY (src_person_id) REFERENCES public.persons(id);


--
-- Name: likes fk_like_person; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT fk_like_person FOREIGN KEY (person_id) REFERENCES public.persons(id) ON DELETE CASCADE;


--
-- Name: messages fk_messages_author; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_messages_author FOREIGN KEY (author_id) REFERENCES public.persons(id);


--
-- Name: messages fk_messages_dialog; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_messages_dialog FOREIGN KEY (dialog_id) REFERENCES public.dialogs(id);


--
-- Name: messages fk_messages_recipient; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_messages_recipient FOREIGN KEY (recipient_id) REFERENCES public.persons(id);


--
-- Name: notifications fk_notification_person; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT fk_notification_person FOREIGN KEY (person_id) REFERENCES public.persons(id);


--
-- Name: notifications fk_notification_sender; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT fk_notification_sender FOREIGN KEY (sender_id) REFERENCES public.persons(id);


--
-- Name: posts fk_person; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT fk_person FOREIGN KEY (author_id) REFERENCES public.persons(id);


--
-- Name: persons fk_person_settings; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.persons
    ADD CONSTRAINT fk_person_settings FOREIGN KEY (person_settings_id) REFERENCES public.person_settings(id);


--
-- Name: post2tag fk_post2tag_post; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.post2tag
    ADD CONSTRAINT fk_post2tag_post FOREIGN KEY (post_id) REFERENCES public.posts(id) ON DELETE CASCADE;


--
-- Name: post2tag fk_post2tag_tag; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY public.post2tag
    ADD CONSTRAINT fk_post2tag_tag FOREIGN KEY (tag_id) REFERENCES public.tags(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

