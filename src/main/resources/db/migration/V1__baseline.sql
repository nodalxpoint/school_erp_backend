--
-- PostgreSQL database dump
--


-- Dumped from database version 18.4
-- Dumped by pg_dump version 18.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_table_access_method = heap;

--
-- Name: academic_sessions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.academic_sessions (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    end_date date NOT NULL,
    is_active boolean,
    session_name character varying(255) NOT NULL,
    start_date date NOT NULL,
    updated_at timestamp(6) without time zone,
    school_id uuid NOT NULL
);


--
-- Name: attendance; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.attendance (
    id uuid NOT NULL,
    academic_session_id uuid NOT NULL,
    attendance_date date NOT NULL,
    created_at timestamp(6) without time zone,
    marked_by uuid,
    remarks character varying(255),
    status character varying(255),
    submitted_by uuid,
    class_id uuid NOT NULL,
    section_id uuid NOT NULL,
    student_id uuid NOT NULL
);


--
-- Name: class_teacher_assignments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.class_teacher_assignments (
    id uuid NOT NULL,
    academic_session_id uuid NOT NULL,
    class_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    section_id uuid NOT NULL,
    teacher_id uuid NOT NULL
);


--
-- Name: classes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.classes (
    id uuid NOT NULL,
    class_name character varying(50) NOT NULL,
    created_at timestamp(6) without time zone,
    school_id uuid NOT NULL
);


--
-- Name: exam_subjects; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.exam_subjects (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    exam_date date,
    exam_day character varying(20),
    max_marks integer NOT NULL,
    passing_marks integer NOT NULL,
    class_id uuid NOT NULL,
    exam_id uuid NOT NULL,
    subject_id uuid NOT NULL
);


--
-- Name: exams; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.exams (
    id uuid NOT NULL,
    academic_session_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    end_date date,
    exam_name character varying(100) NOT NULL,
    is_active character varying(255),
    start_date date,
    school_id uuid NOT NULL
);


--
-- Name: fee_structures; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fee_structures (
    id uuid NOT NULL,
    academic_session_id uuid,
    amount numeric(10,2) NOT NULL,
    created_at timestamp(6) without time zone,
    due_date date,
    fee_name character varying(100),
    frequency character varying(20),
    class_id uuid,
    school_id uuid NOT NULL,
    CONSTRAINT fee_structures_frequency_check CHECK (((frequency)::text = ANY ((ARRAY['MONTHLY'::character varying, 'QUARTERLY'::character varying, 'HALF_YEARLY'::character varying, 'ANNUALLY'::character varying, 'ONE_TIME'::character varying])::text[])))
);


--
-- Name: notices; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notices (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    created_by uuid,
    description text NOT NULL,
    expiry_date date,
    publish_date date,
    target_type character varying(50),
    title character varying(255) NOT NULL,
    school_id uuid NOT NULL
);


--
-- Name: parents; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.parents (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    emergency_contact character varying(255),
    father_name character varying(255),
    mother_name character varying(255),
    school_id uuid NOT NULL,
    user_id uuid
);


--
-- Name: school_features; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.school_features (
    school_id uuid NOT NULL,
    enabled boolean NOT NULL,
    feature_key character varying(255) NOT NULL,
    CONSTRAINT school_features_feature_key_check CHECK (((feature_key)::text = ANY ((ARRAY['STUDENTS'::character varying, 'TEACHERS'::character varying, 'SUBJECTS'::character varying, 'TIMETABLE'::character varying, 'CLASSES'::character varying, 'ATTENDANCE'::character varying, 'EXAMS'::character varying, 'FEES'::character varying, 'UDISE'::character varying, 'REPORTS'::character varying, 'HOMEWORK'::character varying, 'NOTICES'::character varying])::text[])))
);


--
-- Name: schools; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.schools (
    id uuid NOT NULL,
    address text,
    city character varying(255),
    country character varying(255),
    created_at timestamp(6) without time zone,
    email character varying(255),
    is_deleted boolean,
    logo_url text,
    phone character varying(255),
    school_code character varying(255) NOT NULL,
    school_name character varying(255) NOT NULL,
    state character varying(255),
    updated_at timestamp(6) without time zone
);


--
-- Name: sections; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sections (
    id uuid NOT NULL,
    class_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    section_name character varying(255) NOT NULL
);


--
-- Name: student_enrollments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.student_enrollments (
    id uuid NOT NULL,
    academic_session_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    enrollment_status character varying(255),
    roll_no character varying(255),
    class_id uuid,
    section_id uuid,
    student_id uuid NOT NULL
);


--
-- Name: student_fees; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.student_fees (
    id uuid NOT NULL,
    amount numeric(10,2) NOT NULL,
    created_at timestamp(6) without time zone,
    due_date date,
    fee_month integer NOT NULL,
    fee_year integer NOT NULL,
    paid_amount numeric(10,2),
    paid_at timestamp(6) without time zone,
    payment_status character varying(20),
    remarks text,
    updated_at timestamp(6) without time zone,
    academic_session_id uuid NOT NULL,
    fee_structure_id uuid,
    school_id uuid NOT NULL,
    student_id uuid NOT NULL,
    CONSTRAINT student_fees_payment_status_check CHECK (((payment_status)::text = ANY ((ARRAY['PENDING'::character varying, 'PAID'::character varying, 'PARTIAL'::character varying, 'WAIVED'::character varying])::text[])))
);


--
-- Name: student_marks; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.student_marks (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    is_finalized boolean NOT NULL,
    finalized_at timestamp(6) without time zone,
    finalized_by uuid,
    marks_obtained numeric(5,2) NOT NULL,
    remarks text,
    total_marks numeric(5,2),
    exam_id uuid,
    exam_subject_id uuid NOT NULL,
    student_id uuid NOT NULL
);


--
-- Name: student_progression; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.student_progression (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    evaluated_at timestamp(6) without time zone,
    remarks text,
    status character varying(10) NOT NULL,
    updated_at timestamp(6) without time zone,
    academic_session_id uuid NOT NULL,
    class_id uuid NOT NULL,
    evaluated_by uuid,
    section_id uuid NOT NULL,
    student_id uuid NOT NULL
);


--
-- Name: student_udise_details; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.student_udise_details (
    id uuid NOT NULL,
    aadhaar_last_four character varying(4),
    apaar_id character varying(30),
    bpl_beneficiary boolean,
    created_at timestamp(6) without time zone,
    cwsn boolean,
    disability_type character varying(100),
    ews_disadvantaged boolean,
    indian_national boolean,
    minority_group character varying(100),
    mother_tongue character varying(100),
    name_as_per_aadhaar character varying(255),
    out_of_school_current_year boolean,
    out_of_school_previous_year boolean,
    pen character varying(20),
    pincode character varying(20),
    social_category character varying(50),
    udise_status character varying(20),
    updated_at timestamp(6) without time zone,
    verified_at timestamp(6) without time zone,
    verified_by uuid,
    academic_session_id uuid NOT NULL,
    school_id uuid NOT NULL,
    student_id uuid NOT NULL
);


--
-- Name: students; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.students (
    id uuid NOT NULL,
    admission_date date,
    admission_no character varying(255) NOT NULL,
    created_at timestamp(6) without time zone,
    dob date,
    first_name character varying(255) NOT NULL,
    gender character varying(255),
    is_deleted boolean,
    last_name character varying(255),
    status character varying(255),
    updated_at timestamp(6) without time zone,
    parent_id uuid,
    school_id uuid NOT NULL
);


--
-- Name: subjects; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.subjects (
    id uuid NOT NULL,
    subject_code character varying(50),
    created_at timestamp(6) without time zone,
    is_deleted boolean,
    subject_name character varying(100) NOT NULL,
    school_id uuid NOT NULL
);


--
-- Name: teacher_subject_assignments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.teacher_subject_assignments (
    id uuid NOT NULL,
    academic_session_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    class_id uuid NOT NULL,
    section_id uuid NOT NULL,
    subject_id uuid NOT NULL,
    teacher_id uuid NOT NULL
);


--
-- Name: teacher_timetable; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.teacher_timetable (
    id uuid NOT NULL,
    academic_session_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    day_of_week character varying(20) NOT NULL,
    end_time time(6) without time zone NOT NULL,
    period integer NOT NULL,
    room_no character varying(50),
    start_time time(6) without time zone NOT NULL,
    updated_at timestamp(6) without time zone,
    class_id uuid,
    section_id uuid,
    subject_id uuid,
    teacher_id uuid
);


--
-- Name: teachers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.teachers (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    employee_code character varying(50),
    joining_date date,
    qualification character varying(255),
    school_id uuid NOT NULL,
    user_id uuid
);


--
-- Name: timetable_entries; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.timetable_entries (
    id uuid NOT NULL,
    academic_session_id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    day_of_week character varying(20) NOT NULL,
    end_time time(6) without time zone NOT NULL,
    period integer NOT NULL,
    room_no character varying(50),
    start_time time(6) without time zone NOT NULL,
    class_id uuid,
    section_id uuid,
    subject_id uuid,
    teacher_id uuid
);


--
-- Name: uploaded_files; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.uploaded_files (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    file_name character varying(255) NOT NULL,
    file_path character varying(500) NOT NULL,
    file_size bigint NOT NULL,
    file_type character varying(100) NOT NULL
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    email character varying(255) NOT NULL,
    first_name character varying(100) NOT NULL,
    is_active boolean NOT NULL,
    last_name character varying(100),
    pass_key character varying(255),
    password_hash character varying(255) NOT NULL,
    phone character varying(255),
    platform_admin_access_level character varying(255),
    role character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone,
    school_id uuid,
    CONSTRAINT users_platform_admin_access_level_check CHECK (((platform_admin_access_level)::text = ANY ((ARRAY['EDIT'::character varying, 'VIEW_ONLY'::character varying])::text[]))),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['PLATFORM_ADMIN'::character varying, 'SUPER_ADMIN'::character varying, 'SCHOOL_ADMIN'::character varying, 'TEACHER'::character varying, 'PARENT'::character varying, 'ACCOUNTANT'::character varying])::text[])))
);


--
-- Name: academic_sessions academic_sessions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.academic_sessions
    ADD CONSTRAINT academic_sessions_pkey PRIMARY KEY (id);


--
-- Name: attendance attendance_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT attendance_pkey PRIMARY KEY (id);


--
-- Name: class_teacher_assignments class_teacher_assignments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.class_teacher_assignments
    ADD CONSTRAINT class_teacher_assignments_pkey PRIMARY KEY (id);


--
-- Name: classes classes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.classes
    ADD CONSTRAINT classes_pkey PRIMARY KEY (id);


--
-- Name: exam_subjects exam_subjects_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exam_subjects
    ADD CONSTRAINT exam_subjects_pkey PRIMARY KEY (id);


--
-- Name: exams exams_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exams
    ADD CONSTRAINT exams_pkey PRIMARY KEY (id);


--
-- Name: fee_structures fee_structures_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fee_structures
    ADD CONSTRAINT fee_structures_pkey PRIMARY KEY (id);


--
-- Name: notices notices_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notices
    ADD CONSTRAINT notices_pkey PRIMARY KEY (id);


--
-- Name: parents parents_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.parents
    ADD CONSTRAINT parents_pkey PRIMARY KEY (id);


--
-- Name: school_features school_features_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_features
    ADD CONSTRAINT school_features_pkey PRIMARY KEY (school_id, feature_key);


--
-- Name: schools schools_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schools
    ADD CONSTRAINT schools_pkey PRIMARY KEY (id);


--
-- Name: sections sections_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sections
    ADD CONSTRAINT sections_pkey PRIMARY KEY (id);


--
-- Name: student_enrollments student_enrollments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_enrollments
    ADD CONSTRAINT student_enrollments_pkey PRIMARY KEY (id);


--
-- Name: student_fees student_fees_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_fees
    ADD CONSTRAINT student_fees_pkey PRIMARY KEY (id);


--
-- Name: student_marks student_marks_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_marks
    ADD CONSTRAINT student_marks_pkey PRIMARY KEY (id);


--
-- Name: student_progression student_progression_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_progression
    ADD CONSTRAINT student_progression_pkey PRIMARY KEY (id);


--
-- Name: student_udise_details student_udise_details_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_udise_details
    ADD CONSTRAINT student_udise_details_pkey PRIMARY KEY (id);


--
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);


--
-- Name: subjects subjects_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_pkey PRIMARY KEY (id);


--
-- Name: teacher_subject_assignments teacher_subject_assignments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_subject_assignments
    ADD CONSTRAINT teacher_subject_assignments_pkey PRIMARY KEY (id);


--
-- Name: teacher_timetable teacher_timetable_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_timetable
    ADD CONSTRAINT teacher_timetable_pkey PRIMARY KEY (id);


--
-- Name: teachers teachers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_pkey PRIMARY KEY (id);


--
-- Name: timetable_entries timetable_entries_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.timetable_entries
    ADD CONSTRAINT timetable_entries_pkey PRIMARY KEY (id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: teachers uk7t24gk99athoebhski2vfja0s; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT uk7t24gk99athoebhski2vfja0s UNIQUE (employee_code);


--
-- Name: student_fees uk_student_fee_structure_month_year; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_fees
    ADD CONSTRAINT uk_student_fee_structure_month_year UNIQUE (student_id, fee_structure_id, fee_month, fee_year);


--
-- Name: student_progression uk_student_session_progression; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_progression
    ADD CONSTRAINT uk_student_session_progression UNIQUE (student_id, academic_session_id);


--
-- Name: parents ukc1t2v6wf187l8w0yew9sph3l4; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.parents
    ADD CONSTRAINT ukc1t2v6wf187l8w0yew9sph3l4 UNIQUE (user_id);


--
-- Name: teachers ukcd1k6xwg9jqtiwx9ybnxpmoh9; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT ukcd1k6xwg9jqtiwx9ybnxpmoh9 UNIQUE (user_id);


--
-- Name: users ukdu5v5sr43g5bfnji4vb8hg5s3; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukdu5v5sr43g5bfnji4vb8hg5s3 UNIQUE (phone);


--
-- Name: students ukmld7jr7tg6pbaehxih5dofgpr; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT ukmld7jr7tg6pbaehxih5dofgpr UNIQUE (admission_no);


--
-- Name: schools ukouqsuih3yotj0anrhnc4rsbbv; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schools
    ADD CONSTRAINT ukouqsuih3yotj0anrhnc4rsbbv UNIQUE (school_code);


--
-- Name: uploaded_files uploaded_files_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.uploaded_files
    ADD CONSTRAINT uploaded_files_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: student_marks fk24h4c5c5ytlsa8obdddak3q6t; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_marks
    ADD CONSTRAINT fk24h4c5c5ytlsa8obdddak3q6t FOREIGN KEY (exam_subject_id) REFERENCES public.exam_subjects(id);


--
-- Name: teachers fk25tvrvw3ww2p7mbt62abrbwev; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT fk25tvrvw3ww2p7mbt62abrbwev FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: student_progression fk2itc4rpm0ri0sa168uybh9c99; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_progression
    ADD CONSTRAINT fk2itc4rpm0ri0sa168uybh9c99 FOREIGN KEY (section_id) REFERENCES public.sections(id);


--
-- Name: student_progression fk34v15d504gloqxbfyojhq1k7q; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_progression
    ADD CONSTRAINT fk34v15d504gloqxbfyojhq1k7q FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: users fk3gj5j7vnsoxf1wp9n5hsqdiq3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk3gj5j7vnsoxf1wp9n5hsqdiq3 FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: student_progression fk3w8hnippbbgkoytlxplft9tew; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_progression
    ADD CONSTRAINT fk3w8hnippbbgkoytlxplft9tew FOREIGN KEY (academic_session_id) REFERENCES public.academic_sessions(id);


--
-- Name: student_enrollments fk4d15ag1nk7we74ucre2asjeux; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_enrollments
    ADD CONSTRAINT fk4d15ag1nk7we74ucre2asjeux FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: teacher_timetable fk4j4qisb53wk11ajtpjcmvjvd9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_timetable
    ADD CONSTRAINT fk4j4qisb53wk11ajtpjcmvjvd9 FOREIGN KEY (subject_id) REFERENCES public.subjects(id);


--
-- Name: exams fk58snu3x30ly9owm86j8bqbrd2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exams
    ADD CONSTRAINT fk58snu3x30ly9owm86j8bqbrd2 FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: fee_structures fk5cgvonnjlrpfixvs1dxwyngys; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fee_structures
    ADD CONSTRAINT fk5cgvonnjlrpfixvs1dxwyngys FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: attendance fk7121lveuhtmu9wa6m90ayd5yg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT fk7121lveuhtmu9wa6m90ayd5yg FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: students fk7bbpphkk8f0aoav3iiih3mh4e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT fk7bbpphkk8f0aoav3iiih3mh4e FOREIGN KEY (parent_id) REFERENCES public.parents(id);


--
-- Name: student_marks fk7elianinpjywgo4cjspy6crjf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_marks
    ADD CONSTRAINT fk7elianinpjywgo4cjspy6crjf FOREIGN KEY (exam_id) REFERENCES public.exams(id);


--
-- Name: student_fees fk7h3isyb24c6v4rspltbe1xua5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_fees
    ADD CONSTRAINT fk7h3isyb24c6v4rspltbe1xua5 FOREIGN KEY (fee_structure_id) REFERENCES public.fee_structures(id);


--
-- Name: timetable_entries fk7wdiylh6n96rvfmsndtoe0u2d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.timetable_entries
    ADD CONSTRAINT fk7wdiylh6n96rvfmsndtoe0u2d FOREIGN KEY (teacher_id) REFERENCES public.teachers(id);


--
-- Name: teacher_timetable fk84htegwd8phke4nv34exg31ls; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_timetable
    ADD CONSTRAINT fk84htegwd8phke4nv34exg31ls FOREIGN KEY (section_id) REFERENCES public.sections(id);


--
-- Name: notices fk98pwy06fm1aicue0jqqloin89; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notices
    ADD CONSTRAINT fk98pwy06fm1aicue0jqqloin89 FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: timetable_entries fk994txt56oef2mnnmaivvc3ej4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.timetable_entries
    ADD CONSTRAINT fk994txt56oef2mnnmaivvc3ej4 FOREIGN KEY (section_id) REFERENCES public.sections(id);


--
-- Name: academic_sessions fkarqkntvfvtb24ymwy5isisem3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.academic_sessions
    ADD CONSTRAINT fkarqkntvfvtb24ymwy5isisem3 FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: parents fkatq0lg3m5wavlfe3p7kfkvwpj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.parents
    ADD CONSTRAINT fkatq0lg3m5wavlfe3p7kfkvwpj FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: teachers fkb8dct7w2j1vl1r2bpstw5isc0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT fkb8dct7w2j1vl1r2bpstw5isc0 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: student_fees fkbotp74x1rs66lldvu21nnc1vk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_fees
    ADD CONSTRAINT fkbotp74x1rs66lldvu21nnc1vk FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: parents fkchh8tf8w072tapgqoijrahojk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.parents
    ADD CONSTRAINT fkchh8tf8w072tapgqoijrahojk FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: attendance fkdhc0y7i6skyu3xt4jowibdu0n; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT fkdhc0y7i6skyu3xt4jowibdu0n FOREIGN KEY (section_id) REFERENCES public.sections(id);


--
-- Name: students fkdojmg8v3rw2ow4dev2b8q5oqq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT fkdojmg8v3rw2ow4dev2b8q5oqq FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: student_udise_details fkdq167qqwsgblqc8kab9kl0hb0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_udise_details
    ADD CONSTRAINT fkdq167qqwsgblqc8kab9kl0hb0 FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: teacher_subject_assignments fkffvj43vmuwhvden12oukxhy1s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_subject_assignments
    ADD CONSTRAINT fkffvj43vmuwhvden12oukxhy1s FOREIGN KEY (teacher_id) REFERENCES public.teachers(id);


--
-- Name: student_fees fkg9ylysdeqya2o237a0fokxs82; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_fees
    ADD CONSTRAINT fkg9ylysdeqya2o237a0fokxs82 FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: fee_structures fkgmb1e4axfm1bau8kwqaaw6gxs; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fee_structures
    ADD CONSTRAINT fkgmb1e4axfm1bau8kwqaaw6gxs FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: teacher_timetable fkhwg4yqd39ioodeewx6nichiv0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_timetable
    ADD CONSTRAINT fkhwg4yqd39ioodeewx6nichiv0 FOREIGN KEY (teacher_id) REFERENCES public.teachers(id);


--
-- Name: timetable_entries fki8jv4d492h85hygx2v35aytjh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.timetable_entries
    ADD CONSTRAINT fki8jv4d492h85hygx2v35aytjh FOREIGN KEY (subject_id) REFERENCES public.subjects(id);


--
-- Name: teacher_subject_assignments fkii3rjay7d7ltb2ogt4ta8ebk5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_subject_assignments
    ADD CONSTRAINT fkii3rjay7d7ltb2ogt4ta8ebk5 FOREIGN KEY (subject_id) REFERENCES public.subjects(id);


--
-- Name: student_marks fkj4vbax6qk96iabnxlkass7kwo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_marks
    ADD CONSTRAINT fkj4vbax6qk96iabnxlkass7kwo FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: exam_subjects fkj5u6jv9qdatcs3uhq9astybmf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exam_subjects
    ADD CONSTRAINT fkj5u6jv9qdatcs3uhq9astybmf FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: exam_subjects fkjch8aoitlh00suy6iubnwkjr2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exam_subjects
    ADD CONSTRAINT fkjch8aoitlh00suy6iubnwkjr2 FOREIGN KEY (exam_id) REFERENCES public.exams(id);


--
-- Name: teacher_timetable fkkcyv0icumij1vum3y4l8vx4s4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_timetable
    ADD CONSTRAINT fkkcyv0icumij1vum3y4l8vx4s4 FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: student_udise_details fkkprnqcsnkorlvdtokymm65etb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_udise_details
    ADD CONSTRAINT fkkprnqcsnkorlvdtokymm65etb FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: student_udise_details fkllsqm8roetji0uv8gs3j15qca; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_udise_details
    ADD CONSTRAINT fkllsqm8roetji0uv8gs3j15qca FOREIGN KEY (academic_session_id) REFERENCES public.academic_sessions(id);


--
-- Name: teacher_subject_assignments fkmji3exnv07dr58qot1ob47j11; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_subject_assignments
    ADD CONSTRAINT fkmji3exnv07dr58qot1ob47j11 FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: subjects fkmuktvnrq4ft25nduvev1wseqd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT fkmuktvnrq4ft25nduvev1wseqd FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: timetable_entries fknowgf3fjuyhk2j6f40pol17au; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.timetable_entries
    ADD CONSTRAINT fknowgf3fjuyhk2j6f40pol17au FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: student_progression fkojmf5j2j4r5wxcjx6xmvfroiq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_progression
    ADD CONSTRAINT fkojmf5j2j4r5wxcjx6xmvfroiq FOREIGN KEY (evaluated_by) REFERENCES public.teachers(id);


--
-- Name: teacher_subject_assignments fkothmavq73sv68djb0il5rseev; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.teacher_subject_assignments
    ADD CONSTRAINT fkothmavq73sv68djb0il5rseev FOREIGN KEY (section_id) REFERENCES public.sections(id);


--
-- Name: student_fees fkpvcqn6h6eudbncmxk17478mug; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_fees
    ADD CONSTRAINT fkpvcqn6h6eudbncmxk17478mug FOREIGN KEY (academic_session_id) REFERENCES public.academic_sessions(id);


--
-- Name: exam_subjects fkq5m35424eql3p01trdwkaxf2r; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exam_subjects
    ADD CONSTRAINT fkq5m35424eql3p01trdwkaxf2r FOREIGN KEY (subject_id) REFERENCES public.subjects(id);


--
-- Name: school_features fkqovaxph3bhwr3wj5lsts90lrn; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.school_features
    ADD CONSTRAINT fkqovaxph3bhwr3wj5lsts90lrn FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: sections fkrdd11xs7d1ryf2c9r4sete993; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sections
    ADD CONSTRAINT fkrdd11xs7d1ryf2c9r4sete993 FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: student_enrollments fkrvjgsoflhwarcj78oa1ltsr6k; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_enrollments
    ADD CONSTRAINT fkrvjgsoflhwarcj78oa1ltsr6k FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: attendance fkrx58locko31i5sa3goghxssli; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.attendance
    ADD CONSTRAINT fkrx58locko31i5sa3goghxssli FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: student_progression fktj9rr4pflcsov95lubin2gbep; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_progression
    ADD CONSTRAINT fktj9rr4pflcsov95lubin2gbep FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- Name: student_enrollments fktlf8nmnc5mm07jeokpr1yqh2a; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.student_enrollments
    ADD CONSTRAINT fktlf8nmnc5mm07jeokpr1yqh2a FOREIGN KEY (section_id) REFERENCES public.sections(id);


--
-- PostgreSQL database dump complete
--


