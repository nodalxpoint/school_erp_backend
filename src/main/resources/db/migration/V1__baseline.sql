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

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

-- *not* creating schema, since initdb creates it


--
-- Name: AdmissionStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."AdmissionStatus" AS ENUM (
    'ENQUIRY',
    'APPLIED',
    'APPROVED',
    'REJECTED',
    'ENROLLED'
);


--
-- Name: AttemptStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."AttemptStatus" AS ENUM (
    'IN_PROGRESS',
    'SUBMITTED',
    'GRADED'
);


--
-- Name: AttendanceStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."AttendanceStatus" AS ENUM (
    'PRESENT',
    'ABSENT',
    'LATE',
    'HALF_DAY',
    'EXCUSED'
);


--
-- Name: DayOfWeek; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."DayOfWeek" AS ENUM (
    'MONDAY',
    'TUESDAY',
    'WEDNESDAY',
    'THURSDAY',
    'FRIDAY',
    'SATURDAY',
    'SUNDAY'
);


--
-- Name: ExamType; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."ExamType" AS ENUM (
    'UNIT_TEST',
    'MID_TERM',
    'FINAL',
    'ONLINE',
    'ASSIGNMENT'
);


--
-- Name: GateDirection; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."GateDirection" AS ENUM (
    'IN',
    'OUT'
);


--
-- Name: GatePassStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."GatePassStatus" AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED',
    'USED'
);


--
-- Name: Gender; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."Gender" AS ENUM (
    'MALE',
    'FEMALE',
    'OTHER'
);


--
-- Name: ImportKind; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."ImportKind" AS ENUM (
    'STUDENTS',
    'FEES',
    'EMPLOYEES'
);


--
-- Name: ImportState; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."ImportState" AS ENUM (
    'PENDING',
    'COMPLETED',
    'FAILED'
);


--
-- Name: IntegrationProvider; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."IntegrationProvider" AS ENUM (
    'TALLY',
    'QUICKBOOKS',
    'AZURE_AD',
    'GOOGLE_WORKSPACE'
);


--
-- Name: IntegrationStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."IntegrationStatus" AS ENUM (
    'CONNECTED',
    'DISCONNECTED',
    'ERROR'
);


--
-- Name: InvoiceStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."InvoiceStatus" AS ENUM (
    'UNPAID',
    'PARTIAL',
    'PAID',
    'OVERDUE',
    'CANCELLED'
);


--
-- Name: LeaveStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."LeaveStatus" AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED'
);


--
-- Name: LedgerType; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."LedgerType" AS ENUM (
    'INCOME',
    'EXPENSE'
);


--
-- Name: MinorityGroup; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."MinorityGroup" AS ENUM (
    'MUSLIM',
    'CHRISTIAN',
    'SIKH',
    'BUDDHIST',
    'PARSI',
    'JAIN',
    'NOT_APPLICABLE'
);


--
-- Name: NotificationChannel; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."NotificationChannel" AS ENUM (
    'SMS',
    'EMAIL'
);


--
-- Name: NotificationState; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."NotificationState" AS ENUM (
    'QUEUED',
    'SENT',
    'FAILED'
);


--
-- Name: OnlineExamStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."OnlineExamStatus" AS ENUM (
    'DRAFT',
    'PUBLISHED',
    'CLOSED'
);


--
-- Name: PaymentMethod; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."PaymentMethod" AS ENUM (
    'CASH',
    'CARD',
    'UPI',
    'BANK_TRANSFER',
    'CHEQUE',
    'GATEWAY',
    'WALLET'
);


--
-- Name: PaymentStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."PaymentStatus" AS ENUM (
    'PENDING',
    'SUCCESS',
    'FAILED',
    'REFUNDED'
);


--
-- Name: PublishStatus; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."PublishStatus" AS ENUM (
    'DRAFT',
    'PUBLISHED',
    'ARCHIVED'
);


--
-- Name: QuestionType; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."QuestionType" AS ENUM (
    'SINGLE_CHOICE',
    'MULTIPLE_CHOICE',
    'TRUE_FALSE',
    'SHORT_ANSWER'
);


--
-- Name: RoleName; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."RoleName" AS ENUM (
    'SUPER_ADMIN',
    'ADMIN',
    'TEACHER',
    'STUDENT',
    'PARENT',
    'ACCOUNTANT',
    'LIBRARIAN',
    'HR'
);


--
-- Name: SocialCategory; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."SocialCategory" AS ENUM (
    'GENERAL',
    'SC',
    'ST',
    'OBC'
);


--
-- Name: StockTxnType; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."StockTxnType" AS ENUM (
    'RECEIVE',
    'ISSUE',
    'ADJUST'
);


--
-- Name: SyncDirection; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."SyncDirection" AS ENUM (
    'PUSH',
    'PULL',
    'BIDIRECTIONAL'
);


--
-- Name: SyncState; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."SyncState" AS ENUM (
    'PENDING',
    'RUNNING',
    'SUCCESS',
    'FAILED'
);


--
-- Name: YesNo; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public."YesNo" AS ENUM (
    'YES',
    'NO'
);


SET default_table_access_method = heap;

--
-- Name: AcademicYear; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."AcademicYear" (
    id text NOT NULL,
    name text NOT NULL,
    "startDate" timestamp(3) without time zone NOT NULL,
    "endDate" timestamp(3) without time zone NOT NULL,
    "isCurrent" boolean DEFAULT false NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Admission; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Admission" (
    id text NOT NULL,
    "applicationNo" text NOT NULL,
    "firstName" text NOT NULL,
    "lastName" text NOT NULL,
    email text NOT NULL,
    phone text NOT NULL,
    "dateOfBirth" timestamp(3) without time zone,
    gender public."Gender",
    "appliedClassId" text,
    "academicYearId" text NOT NULL,
    status public."AdmissionStatus" DEFAULT 'ENQUIRY'::public."AdmissionStatus" NOT NULL,
    notes text,
    "studentId" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: Attendance; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Attendance" (
    id text NOT NULL,
    "studentId" text NOT NULL,
    date timestamp(3) without time zone NOT NULL,
    status public."AttendanceStatus" NOT NULL,
    remarks text,
    "markedBy" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: AuditLog; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."AuditLog" (
    id text NOT NULL,
    "userId" text,
    action text NOT NULL,
    module text NOT NULL,
    "entityId" text,
    "metaJson" text,
    "ipAddress" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Batch; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Batch" (
    id text NOT NULL,
    name text NOT NULL,
    "courseId" text NOT NULL,
    "startDate" timestamp(3) without time zone NOT NULL,
    "endDate" timestamp(3) without time zone,
    capacity integer DEFAULT 30 NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: BlogPost; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."BlogPost" (
    id text NOT NULL,
    title text NOT NULL,
    slug text NOT NULL,
    excerpt text,
    body text NOT NULL,
    "coverUrl" text,
    status public."PublishStatus" DEFAULT 'DRAFT'::public."PublishStatus" NOT NULL,
    "authorId" text,
    "publishedAt" timestamp(3) without time zone,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: CalendarEvent; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."CalendarEvent" (
    id text NOT NULL,
    title text NOT NULL,
    description text,
    location text,
    "startAt" timestamp(3) without time zone NOT NULL,
    "endAt" timestamp(3) without time zone,
    "allDay" boolean DEFAULT false NOT NULL,
    category text,
    "createdById" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Certificate; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Certificate" (
    id text NOT NULL,
    "studentId" text NOT NULL,
    "templateId" text NOT NULL,
    "certificateNo" text NOT NULL,
    "issuedAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "fileUrl" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: CertificateTemplate; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."CertificateTemplate" (
    id text NOT NULL,
    name text NOT NULL,
    "bodyHtml" text NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Class; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Class" (
    id text NOT NULL,
    name text NOT NULL,
    "academicYearId" text NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: ClassSubject; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."ClassSubject" (
    id text NOT NULL,
    "classId" text NOT NULL,
    "subjectId" text NOT NULL,
    "teacherId" text
);


--
-- Name: Course; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Course" (
    id text NOT NULL,
    name text NOT NULL,
    description text,
    "durationWeeks" integer,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: DiscussionReply; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."DiscussionReply" (
    id text NOT NULL,
    "threadId" text NOT NULL,
    "authorId" text,
    body text NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: DiscussionThread; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."DiscussionThread" (
    id text NOT NULL,
    title text NOT NULL,
    body text NOT NULL,
    "authorId" text,
    "isPinned" boolean DEFAULT false NOT NULL,
    "isLocked" boolean DEFAULT false NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: DocFolder; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."DocFolder" (
    id text NOT NULL,
    name text NOT NULL,
    "parentId" text,
    "createdBy" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Document; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Document" (
    id text NOT NULL,
    "folderId" text,
    title text NOT NULL,
    "fileUrl" text,
    "googleDocId" text,
    "mimeType" text,
    "sizeBytes" integer,
    "uploadedBy" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: Employee; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Employee" (
    id text NOT NULL,
    "userId" text,
    "employeeCode" text NOT NULL,
    "firstName" text NOT NULL,
    "lastName" text NOT NULL,
    email text,
    phone text,
    department text,
    designation text,
    "employmentType" text,
    "joiningDate" timestamp(3) without time zone,
    "baseSalary" double precision DEFAULT 0 NOT NULL,
    "isActive" boolean DEFAULT true NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: Exam; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Exam" (
    id text NOT NULL,
    name text NOT NULL,
    "examType" public."ExamType" NOT NULL,
    "academicYearId" text NOT NULL,
    "startDate" timestamp(3) without time zone NOT NULL,
    "endDate" timestamp(3) without time zone NOT NULL,
    "isOnline" boolean DEFAULT false NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: ExamResult; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."ExamResult" (
    id text NOT NULL,
    "examSubjectId" text NOT NULL,
    "studentId" text NOT NULL,
    "marksObtained" double precision NOT NULL,
    grade text,
    remarks text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: ExamSubject; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."ExamSubject" (
    id text NOT NULL,
    "examId" text NOT NULL,
    "subjectId" text NOT NULL,
    "examDate" timestamp(3) without time zone,
    "maxMarks" double precision DEFAULT 100 NOT NULL,
    "passMarks" double precision DEFAULT 33 NOT NULL
);


--
-- Name: FeeHead; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."FeeHead" (
    id text NOT NULL,
    name text NOT NULL,
    "isRecurring" boolean DEFAULT true NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: FeeInvoice; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."FeeInvoice" (
    id text NOT NULL,
    "invoiceNo" text NOT NULL,
    "studentId" text NOT NULL,
    "feeStructureId" text,
    "issueDate" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "dueDate" timestamp(3) without time zone NOT NULL,
    "totalAmount" double precision NOT NULL,
    "paidAmount" double precision DEFAULT 0 NOT NULL,
    status public."InvoiceStatus" DEFAULT 'UNPAID'::public."InvoiceStatus" NOT NULL,
    notes text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: FeeInvoiceItem; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."FeeInvoiceItem" (
    id text NOT NULL,
    "invoiceId" text NOT NULL,
    "feeHeadId" text NOT NULL,
    amount double precision NOT NULL
);


--
-- Name: FeeStructure; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."FeeStructure" (
    id text NOT NULL,
    name text NOT NULL,
    "classId" text,
    "academicYearId" text NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: FeeStructureItem; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."FeeStructureItem" (
    id text NOT NULL,
    "feeStructureId" text NOT NULL,
    "feeHeadId" text NOT NULL,
    amount double precision NOT NULL,
    "dueDayOfMonth" integer
);


--
-- Name: FeeWallet; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."FeeWallet" (
    id text NOT NULL,
    "studentId" text NOT NULL,
    balance double precision DEFAULT 0 NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: GalleryAlbum; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."GalleryAlbum" (
    id text NOT NULL,
    title text NOT NULL,
    description text,
    "coverUrl" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: GalleryImage; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."GalleryImage" (
    id text NOT NULL,
    "albumId" text NOT NULL,
    url text NOT NULL,
    caption text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: GateLog; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."GateLog" (
    id text NOT NULL,
    "gatePassId" text,
    direction public."GateDirection" NOT NULL,
    "personName" text NOT NULL,
    note text,
    "loggedBy" text,
    "loggedAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: GatePass; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."GatePass" (
    id text NOT NULL,
    "passNo" text NOT NULL,
    "studentId" text,
    "visitorName" text,
    "visitorPhone" text,
    purpose text NOT NULL,
    status public."GatePassStatus" DEFAULT 'PENDING'::public."GatePassStatus" NOT NULL,
    "validFrom" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "validUntil" timestamp(3) without time zone,
    "approvedBy" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: GradeEntry; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."GradeEntry" (
    id text NOT NULL,
    "studentId" text NOT NULL,
    "subjectId" text NOT NULL,
    title text NOT NULL,
    score double precision NOT NULL,
    "maxScore" double precision DEFAULT 100 NOT NULL,
    weight double precision DEFAULT 1 NOT NULL,
    "gradedAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Hostel; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Hostel" (
    id text NOT NULL,
    name text NOT NULL,
    type text DEFAULT 'BOYS'::text NOT NULL,
    warden text,
    address text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: HostelAllocation; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."HostelAllocation" (
    id text NOT NULL,
    "roomId" text NOT NULL,
    "studentId" text NOT NULL,
    "allocatedAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "vacatedAt" timestamp(3) without time zone,
    "isActive" boolean DEFAULT true NOT NULL
);


--
-- Name: HostelRoom; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."HostelRoom" (
    id text NOT NULL,
    "hostelId" text NOT NULL,
    "roomNumber" text NOT NULL,
    capacity integer DEFAULT 2 NOT NULL,
    fee double precision DEFAULT 0 NOT NULL
);


--
-- Name: IdCard; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."IdCard" (
    id text NOT NULL,
    "studentId" text NOT NULL,
    "validFrom" timestamp(3) without time zone NOT NULL,
    "validUntil" timestamp(3) without time zone NOT NULL,
    "fileUrl" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: ImportJob; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."ImportJob" (
    id text NOT NULL,
    kind public."ImportKind" NOT NULL,
    "fileName" text,
    "totalRows" integer DEFAULT 0 NOT NULL,
    "successRows" integer DEFAULT 0 NOT NULL,
    "failedRows" integer DEFAULT 0 NOT NULL,
    state public."ImportState" DEFAULT 'PENDING'::public."ImportState" NOT NULL,
    "errorReport" text,
    "createdById" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: InstitutionSetting; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."InstitutionSetting" (
    id text NOT NULL,
    "schoolName" text,
    "udiseCode" text,
    "affiliationNo" text,
    address text,
    pincode text,
    "contactEmail" text,
    "contactPhone" text,
    "updatedAt" timestamp(3) without time zone NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Integration; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Integration" (
    id text NOT NULL,
    provider public."IntegrationProvider" NOT NULL,
    status public."IntegrationStatus" DEFAULT 'DISCONNECTED'::public."IntegrationStatus" NOT NULL,
    "configJson" text,
    "lastSyncAt" timestamp(3) without time zone,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: InventoryCategory; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."InventoryCategory" (
    id text NOT NULL,
    name text NOT NULL
);


--
-- Name: InventoryItem; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."InventoryItem" (
    id text NOT NULL,
    name text NOT NULL,
    sku text NOT NULL,
    "categoryId" text,
    unit text DEFAULT 'pcs'::text NOT NULL,
    quantity integer DEFAULT 0 NOT NULL,
    "reorderLevel" integer DEFAULT 0 NOT NULL,
    "unitCost" double precision DEFAULT 0 NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: LeaveRequest; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."LeaveRequest" (
    id text NOT NULL,
    "employeeId" text NOT NULL,
    "leaveType" text NOT NULL,
    "fromDate" timestamp(3) without time zone NOT NULL,
    "toDate" timestamp(3) without time zone NOT NULL,
    reason text,
    status public."LeaveStatus" DEFAULT 'PENDING'::public."LeaveStatus" NOT NULL,
    "reviewedBy" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: LedgerEntry; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."LedgerEntry" (
    id text NOT NULL,
    type public."LedgerType" NOT NULL,
    category text NOT NULL,
    amount double precision NOT NULL,
    description text,
    "entryDate" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "createdBy" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Message; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Message" (
    id text NOT NULL,
    "senderId" text NOT NULL,
    "recipientId" text NOT NULL,
    subject text,
    body text NOT NULL,
    "readAt" timestamp(3) without time zone,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: NewsPost; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."NewsPost" (
    id text NOT NULL,
    title text NOT NULL,
    body text NOT NULL,
    status public."PublishStatus" DEFAULT 'PUBLISHED'::public."PublishStatus" NOT NULL,
    pinned boolean DEFAULT false NOT NULL,
    "authorId" text,
    "publishedAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: NotificationLog; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."NotificationLog" (
    id text NOT NULL,
    channel public."NotificationChannel" NOT NULL,
    "toAddress" text NOT NULL,
    subject text,
    body text NOT NULL,
    state public."NotificationState" DEFAULT 'QUEUED'::public."NotificationState" NOT NULL,
    provider text,
    error text,
    "sentById" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: OnlineExam; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."OnlineExam" (
    id text NOT NULL,
    title text NOT NULL,
    description text,
    "subjectId" text,
    "durationMins" integer DEFAULT 30 NOT NULL,
    "totalMarks" double precision DEFAULT 0 NOT NULL,
    status public."OnlineExamStatus" DEFAULT 'DRAFT'::public."OnlineExamStatus" NOT NULL,
    "startAt" timestamp(3) without time zone,
    "endAt" timestamp(3) without time zone,
    "createdBy" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: OnlineExamAnswer; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."OnlineExamAnswer" (
    id text NOT NULL,
    "attemptId" text NOT NULL,
    "questionId" text NOT NULL,
    response text NOT NULL,
    "isCorrect" boolean,
    awarded double precision
);


--
-- Name: OnlineExamAttempt; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."OnlineExamAttempt" (
    id text NOT NULL,
    "examId" text NOT NULL,
    "studentId" text NOT NULL,
    status public."AttemptStatus" DEFAULT 'IN_PROGRESS'::public."AttemptStatus" NOT NULL,
    score double precision,
    "startedAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "submittedAt" timestamp(3) without time zone
);


--
-- Name: OnlineExamOption; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."OnlineExamOption" (
    id text NOT NULL,
    "questionId" text NOT NULL,
    text text NOT NULL,
    "isCorrect" boolean DEFAULT false NOT NULL
);


--
-- Name: OnlineExamQuestion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."OnlineExamQuestion" (
    id text NOT NULL,
    "examId" text NOT NULL,
    "questionType" public."QuestionType" DEFAULT 'SINGLE_CHOICE'::public."QuestionType" NOT NULL,
    text text NOT NULL,
    marks double precision DEFAULT 1 NOT NULL,
    "order" integer DEFAULT 0 NOT NULL,
    "correctAnswer" text
);


--
-- Name: Parent; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Parent" (
    id text NOT NULL,
    "userId" text NOT NULL,
    occupation text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Payment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Payment" (
    id text NOT NULL,
    "receiptNo" text NOT NULL,
    "studentId" text NOT NULL,
    "invoiceId" text,
    amount double precision NOT NULL,
    method public."PaymentMethod" NOT NULL,
    status public."PaymentStatus" DEFAULT 'SUCCESS'::public."PaymentStatus" NOT NULL,
    "isAdvance" boolean DEFAULT false NOT NULL,
    "gatewayOrderId" text,
    "gatewayPaymentId" text,
    "gatewaySignature" text,
    "paidAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Payslip; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Payslip" (
    id text NOT NULL,
    "employeeId" text NOT NULL,
    month integer NOT NULL,
    year integer NOT NULL,
    basic double precision NOT NULL,
    allowances double precision DEFAULT 0 NOT NULL,
    deductions double precision DEFAULT 0 NOT NULL,
    "netPay" double precision NOT NULL,
    "generatedAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Poll; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Poll" (
    id text NOT NULL,
    question text NOT NULL,
    "isClosed" boolean DEFAULT false NOT NULL,
    "createdById" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "expiresAt" timestamp(3) without time zone
);


--
-- Name: PollOption; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."PollOption" (
    id text NOT NULL,
    "pollId" text NOT NULL,
    label text NOT NULL
);


--
-- Name: PollVote; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."PollVote" (
    id text NOT NULL,
    "pollId" text NOT NULL,
    "optionId" text NOT NULL,
    "userId" text NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: RefreshToken; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."RefreshToken" (
    id text NOT NULL,
    token text NOT NULL,
    "userId" text NOT NULL,
    "expiresAt" timestamp(3) without time zone NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Reminder; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Reminder" (
    id text NOT NULL,
    "userId" text NOT NULL,
    title text NOT NULL,
    notes text,
    "remindAt" timestamp(3) without time zone NOT NULL,
    "isDone" boolean DEFAULT false NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Section; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Section" (
    id text NOT NULL,
    name text NOT NULL,
    "classId" text NOT NULL,
    capacity integer DEFAULT 40 NOT NULL
);


--
-- Name: StockTransaction; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."StockTransaction" (
    id text NOT NULL,
    "itemId" text NOT NULL,
    type public."StockTxnType" NOT NULL,
    quantity integer NOT NULL,
    note text,
    "createdBy" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Student; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Student" (
    id text NOT NULL,
    "userId" text NOT NULL,
    "admissionNo" text NOT NULL,
    "rollNumber" text,
    "dateOfBirth" timestamp(3) without time zone,
    gender public."Gender",
    "bloodGroup" text,
    address text,
    "sectionId" text,
    "photoUrl" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: StudentBatch; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."StudentBatch" (
    id text NOT NULL,
    "studentId" text NOT NULL,
    "batchId" text NOT NULL
);


--
-- Name: StudentGuardian; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."StudentGuardian" (
    id text NOT NULL,
    "studentId" text NOT NULL,
    "parentId" text NOT NULL,
    relation text DEFAULT 'Guardian'::text NOT NULL
);


--
-- Name: StudentRemark; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."StudentRemark" (
    id text NOT NULL,
    "studentId" text NOT NULL,
    category text NOT NULL,
    remark text NOT NULL,
    "createdBy" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Subject; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Subject" (
    id text NOT NULL,
    name text NOT NULL,
    code text NOT NULL,
    "isElective" boolean DEFAULT false NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: SyncLog; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."SyncLog" (
    id text NOT NULL,
    "integrationId" text NOT NULL,
    direction public."SyncDirection" NOT NULL,
    entity text NOT NULL,
    state public."SyncState" DEFAULT 'PENDING'::public."SyncState" NOT NULL,
    "recordCount" integer DEFAULT 0 NOT NULL,
    message text,
    "startedAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "finishedAt" timestamp(3) without time zone
);


--
-- Name: Teacher; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Teacher" (
    id text NOT NULL,
    "userId" text NOT NULL,
    "employeeCode" text NOT NULL,
    designation text,
    department text,
    "joiningDate" timestamp(3) without time zone,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: Timetable; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Timetable" (
    id text NOT NULL,
    name text NOT NULL,
    "academicYearId" text NOT NULL,
    "isActive" boolean DEFAULT true NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: TimetableSlot; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TimetableSlot" (
    id text NOT NULL,
    "timetableId" text NOT NULL,
    "sectionId" text NOT NULL,
    "subjectId" text NOT NULL,
    "teacherId" text NOT NULL,
    "dayOfWeek" public."DayOfWeek" NOT NULL,
    "startTime" text NOT NULL,
    "endTime" text NOT NULL,
    room text
);


--
-- Name: TransportAssignment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TransportAssignment" (
    id text NOT NULL,
    "routeId" text NOT NULL,
    "studentId" text NOT NULL,
    "stopName" text,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: TransportRoute; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TransportRoute" (
    id text NOT NULL,
    name text NOT NULL,
    "vehicleId" text,
    fare double precision DEFAULT 0 NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: TransportStop; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."TransportStop" (
    id text NOT NULL,
    "routeId" text NOT NULL,
    name text NOT NULL,
    "pickupTime" text,
    "stopOrder" integer DEFAULT 0 NOT NULL
);


--
-- Name: UdiseProfile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."UdiseProfile" (
    id text NOT NULL,
    "studentId" text NOT NULL,
    "penNo" text,
    "apaarId" text,
    "aadhaarEnc" text,
    "nameAsPerAadhaar" text,
    "studentNameUpper" text,
    "motherName" text,
    "fatherName" text,
    "guardianName" text,
    pincode text,
    "alternateMobile" text,
    "contactEmail" text,
    "motherTongue" text,
    "socialCategory" public."SocialCategory",
    "minorityGroup" public."MinorityGroup" DEFAULT 'NOT_APPLICABLE'::public."MinorityGroup",
    "bplBeneficiary" public."YesNo",
    "aayBeneficiary" public."YesNo",
    "ewsDisadvantaged" public."YesNo",
    "indianNational" public."YesNo" DEFAULT 'YES'::public."YesNo",
    cwsn public."YesNo" DEFAULT 'NO'::public."YesNo",
    "impairmentType" text,
    "hasDisabilityCert" public."YesNo",
    "disabilityPercent" integer,
    "outOfSchoolCurrent" public."YesNo" DEFAULT 'NO'::public."YesNo",
    "outOfSchoolPrevious" public."YesNo" DEFAULT 'NO'::public."YesNo",
    "isVerified" timestamp(3) without time zone,
    "isFrozen" timestamp(3) without time zone,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: User; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."User" (
    id text NOT NULL,
    email text NOT NULL,
    phone text,
    "passwordHash" text,
    "firstName" text NOT NULL,
    "lastName" text NOT NULL,
    "avatarUrl" text,
    "isActive" boolean DEFAULT true NOT NULL,
    "googleId" text,
    role public."RoleName" NOT NULL,
    "lastLoginAt" timestamp(3) without time zone,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "updatedAt" timestamp(3) without time zone NOT NULL
);


--
-- Name: Vehicle; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."Vehicle" (
    id text NOT NULL,
    "registrationNo" text NOT NULL,
    model text,
    capacity integer DEFAULT 0 NOT NULL,
    "driverName" text,
    "driverPhone" text,
    "isActive" boolean DEFAULT true NOT NULL,
    "createdAt" timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: _prisma_migrations; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public._prisma_migrations (
    id character varying(36) NOT NULL,
    checksum character varying(64) NOT NULL,
    finished_at timestamp with time zone,
    migration_name character varying(255) NOT NULL,
    logs text,
    rolled_back_at timestamp with time zone,
    started_at timestamp with time zone DEFAULT now() NOT NULL,
    applied_steps_count integer DEFAULT 0 NOT NULL
);


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
    subject_name character varying(100) NOT NULL,
    school_id uuid NOT NULL,
    is_deleted boolean
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
    password_hash character varying(255) NOT NULL,
    phone character varying(255),
    role character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone,
    school_id uuid,
    pass_key character varying(255),
    platform_admin_access_level character varying(255),
    CONSTRAINT users_platform_admin_access_level_check CHECK (((platform_admin_access_level)::text = ANY ((ARRAY['EDIT'::character varying, 'VIEW_ONLY'::character varying])::text[]))),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['PLATFORM_ADMIN'::character varying, 'SUPER_ADMIN'::character varying, 'SCHOOL_ADMIN'::character varying, 'TEACHER'::character varying, 'PARENT'::character varying, 'ACCOUNTANT'::character varying])::text[])))
);


--
-- Name: AcademicYear AcademicYear_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."AcademicYear"
    ADD CONSTRAINT "AcademicYear_pkey" PRIMARY KEY (id);


--
-- Name: Admission Admission_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Admission"
    ADD CONSTRAINT "Admission_pkey" PRIMARY KEY (id);


--
-- Name: Attendance Attendance_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Attendance"
    ADD CONSTRAINT "Attendance_pkey" PRIMARY KEY (id);


--
-- Name: AuditLog AuditLog_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."AuditLog"
    ADD CONSTRAINT "AuditLog_pkey" PRIMARY KEY (id);


--
-- Name: Batch Batch_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Batch"
    ADD CONSTRAINT "Batch_pkey" PRIMARY KEY (id);


--
-- Name: BlogPost BlogPost_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."BlogPost"
    ADD CONSTRAINT "BlogPost_pkey" PRIMARY KEY (id);


--
-- Name: CalendarEvent CalendarEvent_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."CalendarEvent"
    ADD CONSTRAINT "CalendarEvent_pkey" PRIMARY KEY (id);


--
-- Name: CertificateTemplate CertificateTemplate_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."CertificateTemplate"
    ADD CONSTRAINT "CertificateTemplate_pkey" PRIMARY KEY (id);


--
-- Name: Certificate Certificate_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Certificate"
    ADD CONSTRAINT "Certificate_pkey" PRIMARY KEY (id);


--
-- Name: ClassSubject ClassSubject_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ClassSubject"
    ADD CONSTRAINT "ClassSubject_pkey" PRIMARY KEY (id);


--
-- Name: Class Class_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Class"
    ADD CONSTRAINT "Class_pkey" PRIMARY KEY (id);


--
-- Name: Course Course_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Course"
    ADD CONSTRAINT "Course_pkey" PRIMARY KEY (id);


--
-- Name: DiscussionReply DiscussionReply_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."DiscussionReply"
    ADD CONSTRAINT "DiscussionReply_pkey" PRIMARY KEY (id);


--
-- Name: DiscussionThread DiscussionThread_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."DiscussionThread"
    ADD CONSTRAINT "DiscussionThread_pkey" PRIMARY KEY (id);


--
-- Name: DocFolder DocFolder_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."DocFolder"
    ADD CONSTRAINT "DocFolder_pkey" PRIMARY KEY (id);


--
-- Name: Document Document_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Document"
    ADD CONSTRAINT "Document_pkey" PRIMARY KEY (id);


--
-- Name: Employee Employee_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Employee"
    ADD CONSTRAINT "Employee_pkey" PRIMARY KEY (id);


--
-- Name: ExamResult ExamResult_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ExamResult"
    ADD CONSTRAINT "ExamResult_pkey" PRIMARY KEY (id);


--
-- Name: ExamSubject ExamSubject_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ExamSubject"
    ADD CONSTRAINT "ExamSubject_pkey" PRIMARY KEY (id);


--
-- Name: Exam Exam_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Exam"
    ADD CONSTRAINT "Exam_pkey" PRIMARY KEY (id);


--
-- Name: FeeHead FeeHead_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeHead"
    ADD CONSTRAINT "FeeHead_pkey" PRIMARY KEY (id);


--
-- Name: FeeInvoiceItem FeeInvoiceItem_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeInvoiceItem"
    ADD CONSTRAINT "FeeInvoiceItem_pkey" PRIMARY KEY (id);


--
-- Name: FeeInvoice FeeInvoice_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeInvoice"
    ADD CONSTRAINT "FeeInvoice_pkey" PRIMARY KEY (id);


--
-- Name: FeeStructureItem FeeStructureItem_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeStructureItem"
    ADD CONSTRAINT "FeeStructureItem_pkey" PRIMARY KEY (id);


--
-- Name: FeeStructure FeeStructure_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeStructure"
    ADD CONSTRAINT "FeeStructure_pkey" PRIMARY KEY (id);


--
-- Name: FeeWallet FeeWallet_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeWallet"
    ADD CONSTRAINT "FeeWallet_pkey" PRIMARY KEY (id);


--
-- Name: GalleryAlbum GalleryAlbum_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GalleryAlbum"
    ADD CONSTRAINT "GalleryAlbum_pkey" PRIMARY KEY (id);


--
-- Name: GalleryImage GalleryImage_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GalleryImage"
    ADD CONSTRAINT "GalleryImage_pkey" PRIMARY KEY (id);


--
-- Name: GateLog GateLog_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GateLog"
    ADD CONSTRAINT "GateLog_pkey" PRIMARY KEY (id);


--
-- Name: GatePass GatePass_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GatePass"
    ADD CONSTRAINT "GatePass_pkey" PRIMARY KEY (id);


--
-- Name: GradeEntry GradeEntry_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GradeEntry"
    ADD CONSTRAINT "GradeEntry_pkey" PRIMARY KEY (id);


--
-- Name: HostelAllocation HostelAllocation_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."HostelAllocation"
    ADD CONSTRAINT "HostelAllocation_pkey" PRIMARY KEY (id);


--
-- Name: HostelRoom HostelRoom_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."HostelRoom"
    ADD CONSTRAINT "HostelRoom_pkey" PRIMARY KEY (id);


--
-- Name: Hostel Hostel_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Hostel"
    ADD CONSTRAINT "Hostel_pkey" PRIMARY KEY (id);


--
-- Name: IdCard IdCard_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."IdCard"
    ADD CONSTRAINT "IdCard_pkey" PRIMARY KEY (id);


--
-- Name: ImportJob ImportJob_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ImportJob"
    ADD CONSTRAINT "ImportJob_pkey" PRIMARY KEY (id);


--
-- Name: InstitutionSetting InstitutionSetting_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."InstitutionSetting"
    ADD CONSTRAINT "InstitutionSetting_pkey" PRIMARY KEY (id);


--
-- Name: Integration Integration_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Integration"
    ADD CONSTRAINT "Integration_pkey" PRIMARY KEY (id);


--
-- Name: InventoryCategory InventoryCategory_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."InventoryCategory"
    ADD CONSTRAINT "InventoryCategory_pkey" PRIMARY KEY (id);


--
-- Name: InventoryItem InventoryItem_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."InventoryItem"
    ADD CONSTRAINT "InventoryItem_pkey" PRIMARY KEY (id);


--
-- Name: LeaveRequest LeaveRequest_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."LeaveRequest"
    ADD CONSTRAINT "LeaveRequest_pkey" PRIMARY KEY (id);


--
-- Name: LedgerEntry LedgerEntry_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."LedgerEntry"
    ADD CONSTRAINT "LedgerEntry_pkey" PRIMARY KEY (id);


--
-- Name: Message Message_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Message"
    ADD CONSTRAINT "Message_pkey" PRIMARY KEY (id);


--
-- Name: NewsPost NewsPost_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."NewsPost"
    ADD CONSTRAINT "NewsPost_pkey" PRIMARY KEY (id);


--
-- Name: NotificationLog NotificationLog_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."NotificationLog"
    ADD CONSTRAINT "NotificationLog_pkey" PRIMARY KEY (id);


--
-- Name: OnlineExamAnswer OnlineExamAnswer_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamAnswer"
    ADD CONSTRAINT "OnlineExamAnswer_pkey" PRIMARY KEY (id);


--
-- Name: OnlineExamAttempt OnlineExamAttempt_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamAttempt"
    ADD CONSTRAINT "OnlineExamAttempt_pkey" PRIMARY KEY (id);


--
-- Name: OnlineExamOption OnlineExamOption_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamOption"
    ADD CONSTRAINT "OnlineExamOption_pkey" PRIMARY KEY (id);


--
-- Name: OnlineExamQuestion OnlineExamQuestion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamQuestion"
    ADD CONSTRAINT "OnlineExamQuestion_pkey" PRIMARY KEY (id);


--
-- Name: OnlineExam OnlineExam_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExam"
    ADD CONSTRAINT "OnlineExam_pkey" PRIMARY KEY (id);


--
-- Name: Parent Parent_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Parent"
    ADD CONSTRAINT "Parent_pkey" PRIMARY KEY (id);


--
-- Name: Payment Payment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Payment"
    ADD CONSTRAINT "Payment_pkey" PRIMARY KEY (id);


--
-- Name: Payslip Payslip_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Payslip"
    ADD CONSTRAINT "Payslip_pkey" PRIMARY KEY (id);


--
-- Name: PollOption PollOption_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."PollOption"
    ADD CONSTRAINT "PollOption_pkey" PRIMARY KEY (id);


--
-- Name: PollVote PollVote_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."PollVote"
    ADD CONSTRAINT "PollVote_pkey" PRIMARY KEY (id);


--
-- Name: Poll Poll_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Poll"
    ADD CONSTRAINT "Poll_pkey" PRIMARY KEY (id);


--
-- Name: RefreshToken RefreshToken_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RefreshToken"
    ADD CONSTRAINT "RefreshToken_pkey" PRIMARY KEY (id);


--
-- Name: Reminder Reminder_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Reminder"
    ADD CONSTRAINT "Reminder_pkey" PRIMARY KEY (id);


--
-- Name: Section Section_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Section"
    ADD CONSTRAINT "Section_pkey" PRIMARY KEY (id);


--
-- Name: StockTransaction StockTransaction_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StockTransaction"
    ADD CONSTRAINT "StockTransaction_pkey" PRIMARY KEY (id);


--
-- Name: StudentBatch StudentBatch_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StudentBatch"
    ADD CONSTRAINT "StudentBatch_pkey" PRIMARY KEY (id);


--
-- Name: StudentGuardian StudentGuardian_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StudentGuardian"
    ADD CONSTRAINT "StudentGuardian_pkey" PRIMARY KEY (id);


--
-- Name: StudentRemark StudentRemark_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StudentRemark"
    ADD CONSTRAINT "StudentRemark_pkey" PRIMARY KEY (id);


--
-- Name: Student Student_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Student"
    ADD CONSTRAINT "Student_pkey" PRIMARY KEY (id);


--
-- Name: Subject Subject_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Subject"
    ADD CONSTRAINT "Subject_pkey" PRIMARY KEY (id);


--
-- Name: SyncLog SyncLog_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."SyncLog"
    ADD CONSTRAINT "SyncLog_pkey" PRIMARY KEY (id);


--
-- Name: Teacher Teacher_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Teacher"
    ADD CONSTRAINT "Teacher_pkey" PRIMARY KEY (id);


--
-- Name: TimetableSlot TimetableSlot_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TimetableSlot"
    ADD CONSTRAINT "TimetableSlot_pkey" PRIMARY KEY (id);


--
-- Name: Timetable Timetable_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Timetable"
    ADD CONSTRAINT "Timetable_pkey" PRIMARY KEY (id);


--
-- Name: TransportAssignment TransportAssignment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TransportAssignment"
    ADD CONSTRAINT "TransportAssignment_pkey" PRIMARY KEY (id);


--
-- Name: TransportRoute TransportRoute_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TransportRoute"
    ADD CONSTRAINT "TransportRoute_pkey" PRIMARY KEY (id);


--
-- Name: TransportStop TransportStop_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TransportStop"
    ADD CONSTRAINT "TransportStop_pkey" PRIMARY KEY (id);


--
-- Name: UdiseProfile UdiseProfile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."UdiseProfile"
    ADD CONSTRAINT "UdiseProfile_pkey" PRIMARY KEY (id);


--
-- Name: User User_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."User"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (id);


--
-- Name: Vehicle Vehicle_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Vehicle"
    ADD CONSTRAINT "Vehicle_pkey" PRIMARY KEY (id);


--
-- Name: _prisma_migrations _prisma_migrations_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public._prisma_migrations
    ADD CONSTRAINT _prisma_migrations_pkey PRIMARY KEY (id);


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
-- Name: Admission_applicationNo_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Admission_applicationNo_key" ON public."Admission" USING btree ("applicationNo");


--
-- Name: Admission_status_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Admission_status_idx" ON public."Admission" USING btree (status);


--
-- Name: Admission_studentId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Admission_studentId_key" ON public."Admission" USING btree ("studentId");


--
-- Name: Attendance_date_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Attendance_date_idx" ON public."Attendance" USING btree (date);


--
-- Name: Attendance_studentId_date_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Attendance_studentId_date_key" ON public."Attendance" USING btree ("studentId", date);


--
-- Name: AuditLog_createdAt_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "AuditLog_createdAt_idx" ON public."AuditLog" USING btree ("createdAt");


--
-- Name: AuditLog_module_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "AuditLog_module_idx" ON public."AuditLog" USING btree (module);


--
-- Name: BlogPost_slug_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "BlogPost_slug_key" ON public."BlogPost" USING btree (slug);


--
-- Name: BlogPost_status_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "BlogPost_status_idx" ON public."BlogPost" USING btree (status);


--
-- Name: CalendarEvent_startAt_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "CalendarEvent_startAt_idx" ON public."CalendarEvent" USING btree ("startAt");


--
-- Name: Certificate_certificateNo_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Certificate_certificateNo_key" ON public."Certificate" USING btree ("certificateNo");


--
-- Name: ClassSubject_classId_subjectId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "ClassSubject_classId_subjectId_key" ON public."ClassSubject" USING btree ("classId", "subjectId");


--
-- Name: Class_name_academicYearId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Class_name_academicYearId_key" ON public."Class" USING btree (name, "academicYearId");


--
-- Name: DiscussionReply_threadId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "DiscussionReply_threadId_idx" ON public."DiscussionReply" USING btree ("threadId");


--
-- Name: Document_folderId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Document_folderId_idx" ON public."Document" USING btree ("folderId");


--
-- Name: Employee_employeeCode_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Employee_employeeCode_key" ON public."Employee" USING btree ("employeeCode");


--
-- Name: Employee_userId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Employee_userId_key" ON public."Employee" USING btree ("userId");


--
-- Name: ExamResult_examSubjectId_studentId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "ExamResult_examSubjectId_studentId_key" ON public."ExamResult" USING btree ("examSubjectId", "studentId");


--
-- Name: ExamSubject_examId_subjectId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "ExamSubject_examId_subjectId_key" ON public."ExamSubject" USING btree ("examId", "subjectId");


--
-- Name: FeeHead_name_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "FeeHead_name_key" ON public."FeeHead" USING btree (name);


--
-- Name: FeeInvoice_invoiceNo_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "FeeInvoice_invoiceNo_key" ON public."FeeInvoice" USING btree ("invoiceNo");


--
-- Name: FeeInvoice_status_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "FeeInvoice_status_idx" ON public."FeeInvoice" USING btree (status);


--
-- Name: FeeInvoice_studentId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "FeeInvoice_studentId_idx" ON public."FeeInvoice" USING btree ("studentId");


--
-- Name: FeeStructureItem_feeStructureId_feeHeadId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "FeeStructureItem_feeStructureId_feeHeadId_key" ON public."FeeStructureItem" USING btree ("feeStructureId", "feeHeadId");


--
-- Name: FeeStructure_academicYearId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "FeeStructure_academicYearId_idx" ON public."FeeStructure" USING btree ("academicYearId");


--
-- Name: FeeWallet_studentId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "FeeWallet_studentId_key" ON public."FeeWallet" USING btree ("studentId");


--
-- Name: GalleryImage_albumId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "GalleryImage_albumId_idx" ON public."GalleryImage" USING btree ("albumId");


--
-- Name: GateLog_loggedAt_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "GateLog_loggedAt_idx" ON public."GateLog" USING btree ("loggedAt");


--
-- Name: GatePass_passNo_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "GatePass_passNo_key" ON public."GatePass" USING btree ("passNo");


--
-- Name: GatePass_status_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "GatePass_status_idx" ON public."GatePass" USING btree (status);


--
-- Name: HostelAllocation_studentId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "HostelAllocation_studentId_idx" ON public."HostelAllocation" USING btree ("studentId");


--
-- Name: HostelRoom_hostelId_roomNumber_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "HostelRoom_hostelId_roomNumber_key" ON public."HostelRoom" USING btree ("hostelId", "roomNumber");


--
-- Name: ImportJob_kind_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "ImportJob_kind_idx" ON public."ImportJob" USING btree (kind);


--
-- Name: Integration_provider_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Integration_provider_key" ON public."Integration" USING btree (provider);


--
-- Name: InventoryCategory_name_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "InventoryCategory_name_key" ON public."InventoryCategory" USING btree (name);


--
-- Name: InventoryItem_sku_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "InventoryItem_sku_key" ON public."InventoryItem" USING btree (sku);


--
-- Name: LeaveRequest_status_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "LeaveRequest_status_idx" ON public."LeaveRequest" USING btree (status);


--
-- Name: LedgerEntry_entryDate_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "LedgerEntry_entryDate_idx" ON public."LedgerEntry" USING btree ("entryDate");


--
-- Name: LedgerEntry_type_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "LedgerEntry_type_idx" ON public."LedgerEntry" USING btree (type);


--
-- Name: Message_recipientId_readAt_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Message_recipientId_readAt_idx" ON public."Message" USING btree ("recipientId", "readAt");


--
-- Name: Message_senderId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Message_senderId_idx" ON public."Message" USING btree ("senderId");


--
-- Name: NewsPost_status_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "NewsPost_status_idx" ON public."NewsPost" USING btree (status);


--
-- Name: NotificationLog_channel_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "NotificationLog_channel_idx" ON public."NotificationLog" USING btree (channel);


--
-- Name: NotificationLog_state_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "NotificationLog_state_idx" ON public."NotificationLog" USING btree (state);


--
-- Name: OnlineExamAnswer_attemptId_questionId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "OnlineExamAnswer_attemptId_questionId_key" ON public."OnlineExamAnswer" USING btree ("attemptId", "questionId");


--
-- Name: OnlineExamAttempt_examId_studentId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "OnlineExamAttempt_examId_studentId_key" ON public."OnlineExamAttempt" USING btree ("examId", "studentId");


--
-- Name: Parent_userId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Parent_userId_key" ON public."Parent" USING btree ("userId");


--
-- Name: Payment_receiptNo_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Payment_receiptNo_key" ON public."Payment" USING btree ("receiptNo");


--
-- Name: Payment_studentId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Payment_studentId_idx" ON public."Payment" USING btree ("studentId");


--
-- Name: Payslip_employeeId_month_year_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Payslip_employeeId_month_year_key" ON public."Payslip" USING btree ("employeeId", month, year);


--
-- Name: PollVote_pollId_userId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "PollVote_pollId_userId_key" ON public."PollVote" USING btree ("pollId", "userId");


--
-- Name: RefreshToken_token_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "RefreshToken_token_key" ON public."RefreshToken" USING btree (token);


--
-- Name: Reminder_userId_remindAt_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Reminder_userId_remindAt_idx" ON public."Reminder" USING btree ("userId", "remindAt");


--
-- Name: Section_name_classId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Section_name_classId_key" ON public."Section" USING btree (name, "classId");


--
-- Name: StockTransaction_itemId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "StockTransaction_itemId_idx" ON public."StockTransaction" USING btree ("itemId");


--
-- Name: StudentBatch_studentId_batchId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "StudentBatch_studentId_batchId_key" ON public."StudentBatch" USING btree ("studentId", "batchId");


--
-- Name: StudentGuardian_studentId_parentId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "StudentGuardian_studentId_parentId_key" ON public."StudentGuardian" USING btree ("studentId", "parentId");


--
-- Name: Student_admissionNo_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Student_admissionNo_key" ON public."Student" USING btree ("admissionNo");


--
-- Name: Student_sectionId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "Student_sectionId_idx" ON public."Student" USING btree ("sectionId");


--
-- Name: Student_userId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Student_userId_key" ON public."Student" USING btree ("userId");


--
-- Name: Subject_code_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Subject_code_key" ON public."Subject" USING btree (code);


--
-- Name: SyncLog_integrationId_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "SyncLog_integrationId_idx" ON public."SyncLog" USING btree ("integrationId");


--
-- Name: Teacher_employeeCode_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Teacher_employeeCode_key" ON public."Teacher" USING btree ("employeeCode");


--
-- Name: Teacher_userId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Teacher_userId_key" ON public."Teacher" USING btree ("userId");


--
-- Name: TimetableSlot_sectionId_dayOfWeek_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "TimetableSlot_sectionId_dayOfWeek_idx" ON public."TimetableSlot" USING btree ("sectionId", "dayOfWeek");


--
-- Name: TransportAssignment_routeId_studentId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "TransportAssignment_routeId_studentId_key" ON public."TransportAssignment" USING btree ("routeId", "studentId");


--
-- Name: UdiseProfile_apaarId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "UdiseProfile_apaarId_key" ON public."UdiseProfile" USING btree ("apaarId");


--
-- Name: UdiseProfile_penNo_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "UdiseProfile_penNo_key" ON public."UdiseProfile" USING btree ("penNo");


--
-- Name: UdiseProfile_studentId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "UdiseProfile_studentId_key" ON public."UdiseProfile" USING btree ("studentId");


--
-- Name: User_email_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "User_email_key" ON public."User" USING btree (email);


--
-- Name: User_googleId_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "User_googleId_key" ON public."User" USING btree ("googleId");


--
-- Name: User_role_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "User_role_idx" ON public."User" USING btree (role);


--
-- Name: Vehicle_registrationNo_key; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX "Vehicle_registrationNo_key" ON public."Vehicle" USING btree ("registrationNo");


--
-- Name: Admission Admission_academicYearId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Admission"
    ADD CONSTRAINT "Admission_academicYearId_fkey" FOREIGN KEY ("academicYearId") REFERENCES public."AcademicYear"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: Admission Admission_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Admission"
    ADD CONSTRAINT "Admission_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: Attendance Attendance_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Attendance"
    ADD CONSTRAINT "Attendance_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: AuditLog AuditLog_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."AuditLog"
    ADD CONSTRAINT "AuditLog_userId_fkey" FOREIGN KEY ("userId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: Batch Batch_courseId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Batch"
    ADD CONSTRAINT "Batch_courseId_fkey" FOREIGN KEY ("courseId") REFERENCES public."Course"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: BlogPost BlogPost_authorId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."BlogPost"
    ADD CONSTRAINT "BlogPost_authorId_fkey" FOREIGN KEY ("authorId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: Certificate Certificate_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Certificate"
    ADD CONSTRAINT "Certificate_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Certificate Certificate_templateId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Certificate"
    ADD CONSTRAINT "Certificate_templateId_fkey" FOREIGN KEY ("templateId") REFERENCES public."CertificateTemplate"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: ClassSubject ClassSubject_classId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ClassSubject"
    ADD CONSTRAINT "ClassSubject_classId_fkey" FOREIGN KEY ("classId") REFERENCES public."Class"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: ClassSubject ClassSubject_subjectId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ClassSubject"
    ADD CONSTRAINT "ClassSubject_subjectId_fkey" FOREIGN KEY ("subjectId") REFERENCES public."Subject"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: ClassSubject ClassSubject_teacherId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ClassSubject"
    ADD CONSTRAINT "ClassSubject_teacherId_fkey" FOREIGN KEY ("teacherId") REFERENCES public."Teacher"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: Class Class_academicYearId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Class"
    ADD CONSTRAINT "Class_academicYearId_fkey" FOREIGN KEY ("academicYearId") REFERENCES public."AcademicYear"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: DiscussionReply DiscussionReply_authorId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."DiscussionReply"
    ADD CONSTRAINT "DiscussionReply_authorId_fkey" FOREIGN KEY ("authorId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: DiscussionReply DiscussionReply_threadId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."DiscussionReply"
    ADD CONSTRAINT "DiscussionReply_threadId_fkey" FOREIGN KEY ("threadId") REFERENCES public."DiscussionThread"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: DiscussionThread DiscussionThread_authorId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."DiscussionThread"
    ADD CONSTRAINT "DiscussionThread_authorId_fkey" FOREIGN KEY ("authorId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: DocFolder DocFolder_parentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."DocFolder"
    ADD CONSTRAINT "DocFolder_parentId_fkey" FOREIGN KEY ("parentId") REFERENCES public."DocFolder"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: Document Document_folderId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Document"
    ADD CONSTRAINT "Document_folderId_fkey" FOREIGN KEY ("folderId") REFERENCES public."DocFolder"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: Employee Employee_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Employee"
    ADD CONSTRAINT "Employee_userId_fkey" FOREIGN KEY ("userId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: ExamResult ExamResult_examSubjectId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ExamResult"
    ADD CONSTRAINT "ExamResult_examSubjectId_fkey" FOREIGN KEY ("examSubjectId") REFERENCES public."ExamSubject"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: ExamResult ExamResult_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ExamResult"
    ADD CONSTRAINT "ExamResult_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: ExamSubject ExamSubject_examId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ExamSubject"
    ADD CONSTRAINT "ExamSubject_examId_fkey" FOREIGN KEY ("examId") REFERENCES public."Exam"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: ExamSubject ExamSubject_subjectId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."ExamSubject"
    ADD CONSTRAINT "ExamSubject_subjectId_fkey" FOREIGN KEY ("subjectId") REFERENCES public."Subject"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: Exam Exam_academicYearId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Exam"
    ADD CONSTRAINT "Exam_academicYearId_fkey" FOREIGN KEY ("academicYearId") REFERENCES public."AcademicYear"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: FeeInvoiceItem FeeInvoiceItem_feeHeadId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeInvoiceItem"
    ADD CONSTRAINT "FeeInvoiceItem_feeHeadId_fkey" FOREIGN KEY ("feeHeadId") REFERENCES public."FeeHead"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: FeeInvoiceItem FeeInvoiceItem_invoiceId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeInvoiceItem"
    ADD CONSTRAINT "FeeInvoiceItem_invoiceId_fkey" FOREIGN KEY ("invoiceId") REFERENCES public."FeeInvoice"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: FeeInvoice FeeInvoice_feeStructureId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeInvoice"
    ADD CONSTRAINT "FeeInvoice_feeStructureId_fkey" FOREIGN KEY ("feeStructureId") REFERENCES public."FeeStructure"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: FeeInvoice FeeInvoice_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeInvoice"
    ADD CONSTRAINT "FeeInvoice_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: FeeStructureItem FeeStructureItem_feeHeadId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeStructureItem"
    ADD CONSTRAINT "FeeStructureItem_feeHeadId_fkey" FOREIGN KEY ("feeHeadId") REFERENCES public."FeeHead"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: FeeStructureItem FeeStructureItem_feeStructureId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeStructureItem"
    ADD CONSTRAINT "FeeStructureItem_feeStructureId_fkey" FOREIGN KEY ("feeStructureId") REFERENCES public."FeeStructure"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: FeeStructure FeeStructure_academicYearId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeStructure"
    ADD CONSTRAINT "FeeStructure_academicYearId_fkey" FOREIGN KEY ("academicYearId") REFERENCES public."AcademicYear"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: FeeWallet FeeWallet_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."FeeWallet"
    ADD CONSTRAINT "FeeWallet_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: GalleryImage GalleryImage_albumId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GalleryImage"
    ADD CONSTRAINT "GalleryImage_albumId_fkey" FOREIGN KEY ("albumId") REFERENCES public."GalleryAlbum"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: GateLog GateLog_gatePassId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GateLog"
    ADD CONSTRAINT "GateLog_gatePassId_fkey" FOREIGN KEY ("gatePassId") REFERENCES public."GatePass"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: GatePass GatePass_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GatePass"
    ADD CONSTRAINT "GatePass_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: GradeEntry GradeEntry_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GradeEntry"
    ADD CONSTRAINT "GradeEntry_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: GradeEntry GradeEntry_subjectId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."GradeEntry"
    ADD CONSTRAINT "GradeEntry_subjectId_fkey" FOREIGN KEY ("subjectId") REFERENCES public."Subject"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: HostelAllocation HostelAllocation_roomId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."HostelAllocation"
    ADD CONSTRAINT "HostelAllocation_roomId_fkey" FOREIGN KEY ("roomId") REFERENCES public."HostelRoom"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: HostelAllocation HostelAllocation_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."HostelAllocation"
    ADD CONSTRAINT "HostelAllocation_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: HostelRoom HostelRoom_hostelId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."HostelRoom"
    ADD CONSTRAINT "HostelRoom_hostelId_fkey" FOREIGN KEY ("hostelId") REFERENCES public."Hostel"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: IdCard IdCard_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."IdCard"
    ADD CONSTRAINT "IdCard_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: InventoryItem InventoryItem_categoryId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."InventoryItem"
    ADD CONSTRAINT "InventoryItem_categoryId_fkey" FOREIGN KEY ("categoryId") REFERENCES public."InventoryCategory"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: LeaveRequest LeaveRequest_employeeId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."LeaveRequest"
    ADD CONSTRAINT "LeaveRequest_employeeId_fkey" FOREIGN KEY ("employeeId") REFERENCES public."Employee"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Message Message_recipientId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Message"
    ADD CONSTRAINT "Message_recipientId_fkey" FOREIGN KEY ("recipientId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Message Message_senderId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Message"
    ADD CONSTRAINT "Message_senderId_fkey" FOREIGN KEY ("senderId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: NewsPost NewsPost_authorId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."NewsPost"
    ADD CONSTRAINT "NewsPost_authorId_fkey" FOREIGN KEY ("authorId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: OnlineExamAnswer OnlineExamAnswer_attemptId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamAnswer"
    ADD CONSTRAINT "OnlineExamAnswer_attemptId_fkey" FOREIGN KEY ("attemptId") REFERENCES public."OnlineExamAttempt"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: OnlineExamAnswer OnlineExamAnswer_questionId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamAnswer"
    ADD CONSTRAINT "OnlineExamAnswer_questionId_fkey" FOREIGN KEY ("questionId") REFERENCES public."OnlineExamQuestion"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: OnlineExamAttempt OnlineExamAttempt_examId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamAttempt"
    ADD CONSTRAINT "OnlineExamAttempt_examId_fkey" FOREIGN KEY ("examId") REFERENCES public."OnlineExam"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: OnlineExamAttempt OnlineExamAttempt_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamAttempt"
    ADD CONSTRAINT "OnlineExamAttempt_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: OnlineExamOption OnlineExamOption_questionId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamOption"
    ADD CONSTRAINT "OnlineExamOption_questionId_fkey" FOREIGN KEY ("questionId") REFERENCES public."OnlineExamQuestion"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: OnlineExamQuestion OnlineExamQuestion_examId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExamQuestion"
    ADD CONSTRAINT "OnlineExamQuestion_examId_fkey" FOREIGN KEY ("examId") REFERENCES public."OnlineExam"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: OnlineExam OnlineExam_subjectId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."OnlineExam"
    ADD CONSTRAINT "OnlineExam_subjectId_fkey" FOREIGN KEY ("subjectId") REFERENCES public."Subject"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: Parent Parent_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Parent"
    ADD CONSTRAINT "Parent_userId_fkey" FOREIGN KEY ("userId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Payment Payment_invoiceId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Payment"
    ADD CONSTRAINT "Payment_invoiceId_fkey" FOREIGN KEY ("invoiceId") REFERENCES public."FeeInvoice"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: Payment Payment_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Payment"
    ADD CONSTRAINT "Payment_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Payslip Payslip_employeeId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Payslip"
    ADD CONSTRAINT "Payslip_employeeId_fkey" FOREIGN KEY ("employeeId") REFERENCES public."Employee"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: PollOption PollOption_pollId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."PollOption"
    ADD CONSTRAINT "PollOption_pollId_fkey" FOREIGN KEY ("pollId") REFERENCES public."Poll"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: PollVote PollVote_optionId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."PollVote"
    ADD CONSTRAINT "PollVote_optionId_fkey" FOREIGN KEY ("optionId") REFERENCES public."PollOption"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: PollVote PollVote_pollId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."PollVote"
    ADD CONSTRAINT "PollVote_pollId_fkey" FOREIGN KEY ("pollId") REFERENCES public."Poll"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: RefreshToken RefreshToken_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."RefreshToken"
    ADD CONSTRAINT "RefreshToken_userId_fkey" FOREIGN KEY ("userId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Reminder Reminder_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Reminder"
    ADD CONSTRAINT "Reminder_userId_fkey" FOREIGN KEY ("userId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Section Section_classId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Section"
    ADD CONSTRAINT "Section_classId_fkey" FOREIGN KEY ("classId") REFERENCES public."Class"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: StockTransaction StockTransaction_itemId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StockTransaction"
    ADD CONSTRAINT "StockTransaction_itemId_fkey" FOREIGN KEY ("itemId") REFERENCES public."InventoryItem"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: StudentBatch StudentBatch_batchId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StudentBatch"
    ADD CONSTRAINT "StudentBatch_batchId_fkey" FOREIGN KEY ("batchId") REFERENCES public."Batch"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: StudentBatch StudentBatch_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StudentBatch"
    ADD CONSTRAINT "StudentBatch_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: StudentGuardian StudentGuardian_parentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StudentGuardian"
    ADD CONSTRAINT "StudentGuardian_parentId_fkey" FOREIGN KEY ("parentId") REFERENCES public."Parent"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: StudentGuardian StudentGuardian_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StudentGuardian"
    ADD CONSTRAINT "StudentGuardian_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: StudentRemark StudentRemark_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."StudentRemark"
    ADD CONSTRAINT "StudentRemark_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Student Student_sectionId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Student"
    ADD CONSTRAINT "Student_sectionId_fkey" FOREIGN KEY ("sectionId") REFERENCES public."Section"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: Student Student_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Student"
    ADD CONSTRAINT "Student_userId_fkey" FOREIGN KEY ("userId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: SyncLog SyncLog_integrationId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."SyncLog"
    ADD CONSTRAINT "SyncLog_integrationId_fkey" FOREIGN KEY ("integrationId") REFERENCES public."Integration"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Teacher Teacher_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Teacher"
    ADD CONSTRAINT "Teacher_userId_fkey" FOREIGN KEY ("userId") REFERENCES public."User"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TimetableSlot TimetableSlot_sectionId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TimetableSlot"
    ADD CONSTRAINT "TimetableSlot_sectionId_fkey" FOREIGN KEY ("sectionId") REFERENCES public."Section"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TimetableSlot TimetableSlot_subjectId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TimetableSlot"
    ADD CONSTRAINT "TimetableSlot_subjectId_fkey" FOREIGN KEY ("subjectId") REFERENCES public."Subject"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: TimetableSlot TimetableSlot_teacherId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TimetableSlot"
    ADD CONSTRAINT "TimetableSlot_teacherId_fkey" FOREIGN KEY ("teacherId") REFERENCES public."Teacher"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: TimetableSlot TimetableSlot_timetableId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TimetableSlot"
    ADD CONSTRAINT "TimetableSlot_timetableId_fkey" FOREIGN KEY ("timetableId") REFERENCES public."Timetable"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: Timetable Timetable_academicYearId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."Timetable"
    ADD CONSTRAINT "Timetable_academicYearId_fkey" FOREIGN KEY ("academicYearId") REFERENCES public."AcademicYear"(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: TransportAssignment TransportAssignment_routeId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TransportAssignment"
    ADD CONSTRAINT "TransportAssignment_routeId_fkey" FOREIGN KEY ("routeId") REFERENCES public."TransportRoute"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TransportAssignment TransportAssignment_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TransportAssignment"
    ADD CONSTRAINT "TransportAssignment_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: TransportRoute TransportRoute_vehicleId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TransportRoute"
    ADD CONSTRAINT "TransportRoute_vehicleId_fkey" FOREIGN KEY ("vehicleId") REFERENCES public."Vehicle"(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: TransportStop TransportStop_routeId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."TransportStop"
    ADD CONSTRAINT "TransportStop_routeId_fkey" FOREIGN KEY ("routeId") REFERENCES public."TransportRoute"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: UdiseProfile UdiseProfile_studentId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."UdiseProfile"
    ADD CONSTRAINT "UdiseProfile_studentId_fkey" FOREIGN KEY ("studentId") REFERENCES public."Student"(id) ON UPDATE CASCADE ON DELETE CASCADE;


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


