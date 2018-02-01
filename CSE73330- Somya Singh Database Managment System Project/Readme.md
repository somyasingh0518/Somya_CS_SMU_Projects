/*
Term Project CSE 7330 - Fall 2017
Somya Singh
Email: somyas@smu.edu
Student ID: 47304053
*/

/* Database Used */
SQLite 3 on top of SQLiteStudio as GUI tool

/* Download */
Download SQLite 3 from https://sqlite.org/download.html
Download SQLiteStudio from https://sqlitestudio.pl/index.rvt?act=download

/* Files with the submission folder */
1. Project Report Phase 1
2. Project Report Phase 2
3. Project Report Phase 3
3. Database Export (in .sql format): SomyaSingh_Sqllit3_CSE7330.sql
4. Database Export (in sqlite3 format): CSE7330_Project.db

/* Challenges faced during project */
1. SQLite only support limited date and time format only (https://sqlite.org/lang_datefunc.html). So to overcome this limitation I had to create triggers forcing user to only use "YYYY-MM-DD" format.
2. SQLite limit only one action in trigger. This forced me to create multiple triggers for Insert, Update and deletes.
3. Since SQLite will treat Varchar(N) as text only, to implement the size limit I used "check" constraint on columns. For e.g on Employee_name i defined it as
    Employee_Name VARCHAR (60) NOT NULL CONSTRAINT CheckConst_len_EmplName CHECK (length(Employee_Name) <= 60),

/* Changes Introduced with Version 3 */
1. One additional columns are introduced for table Employee;
  a. EmployeeStatus VARCHAR ( 30 ) default value 'ActiveEmployee'
2. One additional table "Employee_Exit" was created;
    a. Employee_id FK to Employee(Employee_id)
    b. Exit_date date
