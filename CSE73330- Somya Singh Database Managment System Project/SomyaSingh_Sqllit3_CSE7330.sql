--
-- File generated with SQLiteStudio v3.1.1 on Tue Dec 5 22:50:27 2017
--
-- Text encoding used: UTF-8
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: Component
DROP TABLE IF EXISTS Component;

CREATE TABLE Component (
    Component_ID       INTEGER      PRIMARY KEY AUTOINCREMENT,
    Component_Name     VARCHAR (30) NOT NULL,
    Component_Version  VARCHAR (20) NOT NULL
                                    CONSTRAINT CheckConst_len_CompVersion CHECK (length(Component_Version) = 3),
    Component_Size     INTEGER      NOT NULL,
    Component_Language VARCHAR (30) CONSTRAINT FK_COMPONENT_LANGUAGE REFERENCES Programming_language (Language),
    Owner              INTEGER      CONSTRAINT FK_COMPONENT_OWNER REFERENCES Employee (Employee_ID),
    Component_Status   VARCHAR (20) DEFAULT ('Not-Ready') 
);

INSERT INTO Component (Component_ID, Component_Name, Component_Version, Component_Size, Component_Language, Owner, Component_Status) VALUES (1, 'Keyboard Driver', 'K11', 1200, 'C', 10100, 'Ready');
INSERT INTO Component (Component_ID, Component_Name, Component_Version, Component_Size, Component_Language, Owner, Component_Status) VALUES (2, 'Touch Screen Driver', 'T00', 4000, 'C++', 10100, 'Ready');
INSERT INTO Component (Component_ID, Component_Name, Component_Version, Component_Size, Component_Language, Owner, Component_Status) VALUES (3, 'Dbase Interface', 'D00', 2500, 'C++', 10200, 'Ready');
INSERT INTO Component (Component_ID, Component_Name, Component_Version, Component_Size, Component_Language, Owner, Component_Status) VALUES (4, 'Dbase Interface', 'D01', 2500, 'C++', 10300, 'Ready');
INSERT INTO Component (Component_ID, Component_Name, Component_Version, Component_Size, Component_Language, Owner, Component_Status) VALUES (5, 'Chart generator', 'C11', 6500, 'Java', 10200, 'Not-Ready');
INSERT INTO Component (Component_ID, Component_Name, Component_Version, Component_Size, Component_Language, Owner, Component_Status) VALUES (6, 'Pen driver', 'P01', 3575, 'C', 10400, 'Not-Ready');
INSERT INTO Component (Component_ID, Component_Name, Component_Version, Component_Size, Component_Language, Owner, Component_Status) VALUES (7, 'Math unit', 'A01', 5000, 'C', 10200, 'Usable');
INSERT INTO Component (Component_ID, Component_Name, Component_Version, Component_Size, Component_Language, Owner, Component_Status) VALUES (8, 'Math unit', 'A02', 3500, 'Java', 10200, 'Ready');
INSERT INTO Component (Component_ID, Component_Name, Component_Version, Component_Size, Component_Language, Owner, Component_Status) VALUES (9, 'Dynamic Table Interface', 'D01', 775, 'Javascript', 10400, 'Not-Ready');

-- Table: Employee
DROP TABLE IF EXISTS Employee;

CREATE TABLE Employee (
    Employee_ID    INTEGER      CHECK (length(Employee_ID) = 5),
    Employee_Name  VARCHAR (60) NOT NULL
                                CHECK (length(Employee_Name) <= 60),
    Hire_date      DATE         NOT NULL,
    Manager_ID     INTEGER,
    EmployeeStatus VARCHAR (30) DEFAULT 'ActiveEmployee',
    CONSTRAINT FK_EMPLOYEE_MgrID FOREIGN KEY (
        Manager_ID
    )
    REFERENCES Employee (Employee_ID) ON DELETE SET NULL
                                      ON UPDATE CASCADE,
    PRIMARY KEY (
        Employee_ID
    )
);

INSERT INTO Employee (Employee_ID, Employee_Name, Hire_date, Manager_ID, EmployeeStatus) VALUES (10100, 'Employee-1', '1984-11-08', NULL, 'ActiveEmployee');
INSERT INTO Employee (Employee_ID, Employee_Name, Hire_date, Manager_ID, EmployeeStatus) VALUES (10200, 'Employee-2', '1994-11-08', 10100, 'ActiveEmployee');
INSERT INTO Employee (Employee_ID, Employee_Name, Hire_date, Manager_ID, EmployeeStatus) VALUES (10300, 'Employee-3', '2004-11-08', 10200, 'ActiveEmployee');
INSERT INTO Employee (Employee_ID, Employee_Name, Hire_date, Manager_ID, EmployeeStatus) VALUES (10400, 'Employee-4', '2008-11-01', 10200, 'ActiveEmployee');
INSERT INTO Employee (Employee_ID, Employee_Name, Hire_date, Manager_ID, EmployeeStatus) VALUES (10500, 'Employee-5', '2015-11-01', 10400, 'ActiveEmployee');
INSERT INTO Employee (Employee_ID, Employee_Name, Hire_date, Manager_ID, EmployeeStatus) VALUES (10600, 'Employee-6', '2015-11-01', 10400, 'ActiveEmployee');
INSERT INTO Employee (Employee_ID, Employee_Name, Hire_date, Manager_ID, EmployeeStatus) VALUES (10700, 'Employee-7', '2016-11-01', 10400, 'ExEmployee');
INSERT INTO Employee (Employee_ID, Employee_Name, Hire_date, Manager_ID, EmployeeStatus) VALUES (10800, 'Employee-8', '2017-11-01', 10200, 'ActiveEmployee');

-- Table: Employee_exit
DROP TABLE IF EXISTS Employee_exit;

CREATE TABLE Employee_exit (
    Employee_ID INTEGER REFERENCES Employee (Employee_ID) 
                        PRIMARY KEY
                        NOT NULL,
    Exit_date   DATE    NOT NULL
);

INSERT INTO Employee_exit (Employee_ID, Exit_date) VALUES (10700, '2017-12-05');

-- Table: Inspection
DROP TABLE IF EXISTS Inspection;

CREATE TABLE Inspection (
    Inspected_Component INTEGER REFERENCES Component (Component_ID) ON DELETE CASCADE
                                NOT NULL,
    Inspection_date     DATE    NOT NULL,
    Own_BY              INTEGER REFERENCES Employee (Employee_ID) ON DELETE NO ACTION,
    Score               INTEGER NOT NULL
                                CHECK ( ( (Score >= 0) AND 
                                          (Score <= 100) ) ),
    Description         TEXT    CONSTRAINT CheckConst_len_Description CHECK (length(Description) <= 4000),
    PRIMARY KEY (
        Inspected_Component,
        Inspection_date,
        Own_BY
    )
);

INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (1, '2010-02-14', 10100, 100, 'legacy code which is already approved');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (2, '2017-06-01', 10200, 95, 'initial release ready for usage');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (3, '2010-02-22', 10100, 55, 'too many hard coded parameters, the software must be more maintainable and configurable because we want to use this in other products.');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (3, '2010-02-24', 10100, 78, 'improved, but only handles DB2 format');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (3, '2010-02-26', 10100, 95, 'Okay,handlesDB3format.');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (4, '2011-05-01', 10200, 100, 'Okay ready for use');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (7, '2014-06-10', 10100, 90, 'almost ready');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (8, '2014-06-15', 10100, 70, 'Accuracy problems!');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (8, '2014-06-30', 10100, 100, 'Okay problems fixed');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (8, '2016-11-02', 10700, 100, 're-review for new employee to gain experience in the process.');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (3, '2010-02-28', 10100, 100, 'satisifed');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (6, '2017-07-15', 10300, 80, 'Okay ready for beta testing');
INSERT INTO Inspection (Inspected_Component, Inspection_date, Own_BY, Score, Description) VALUES (6, '2017-08-15', 10400, 60, 'needs rework, introduced new errors');

-- Table: Programming_language
DROP TABLE IF EXISTS Programming_language;

CREATE TABLE Programming_language (
    Language_Status VARCHAR (10),
    Language        VARCHAR (30) PRIMARY KEY
);

INSERT INTO Programming_language (Language_Status, Language) VALUES ('Current', 'C');
INSERT INTO Programming_language (Language_Status, Language) VALUES ('Current', 'C++');
INSERT INTO Programming_language (Language_Status, Language) VALUES ('Current', 'C#');
INSERT INTO Programming_language (Language_Status, Language) VALUES ('Current', 'Java');
INSERT INTO Programming_language (Language_Status, Language) VALUES ('Current', 'PHP');
INSERT INTO Programming_language (Language_Status, Language) VALUES ('Future', 'Python');
INSERT INTO Programming_language (Language_Status, Language) VALUES ('Future', 'assembly');
INSERT INTO Programming_language (Language_Status, Language) VALUES ('Current', 'Javascript');

-- Table: Software_Build
DROP TABLE IF EXISTS Software_Build;

CREATE TABLE Software_Build (
    Software_ID  INTEGER REFERENCES Software_Product (Software_ID),
    Component_ID INTEGER REFERENCES Component (Component_ID) ON DELETE CASCADE
);

INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (1, 1);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (2, 1);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (3, 1);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (4, 1);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (1, 3);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (2, 4);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (2, 6);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (3, 2);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (3, 5);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (4, 2);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (4, 5);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (4, 8);
INSERT INTO Software_Build (Software_ID, Component_ID) VALUES (3, 9);

-- Table: Software_Product
DROP TABLE IF EXISTS Software_Product;

CREATE TABLE Software_Product (
    Software_ID      INTEGER      PRIMARY KEY AUTOINCREMENT,
    Software_Name    VARCHAR (20) NOT NULL,
    Software_Version VARCHAR (20) NOT NULL,
    Product_Status   VARCHAR (20) DEFAULT ('Not-Ready'),
    UNIQUE (
        Software_Name,
        Software_Version
    )
);

INSERT INTO Software_Product (Software_ID, Software_Name, Software_Version, Product_Status) VALUES (1, 'Excel', '2010', 'Ready');
INSERT INTO Software_Product (Software_ID, Software_Name, Software_Version, Product_Status) VALUES (2, 'Excel', '2015', 'Not-Ready');
INSERT INTO Software_Product (Software_ID, Software_Name, Software_Version, Product_Status) VALUES (3, 'Excel', '2018beta', 'Not-Ready');
INSERT INTO Software_Product (Software_ID, Software_Name, Software_Version, Product_Status) VALUES (4, 'Excel', 'secret', 'Not-Ready');

-- Index: idx_component
DROP INDEX IF EXISTS idx_component;

CREATE UNIQUE INDEX idx_component ON Component (
    Component_Name,
    Component_Version
);


-- Index: idx_Employee_ID
DROP INDEX IF EXISTS idx_Employee_ID;

CREATE UNIQUE INDEX idx_Employee_ID ON Employee (
    Employee_ID
);


-- Index: idx_language
DROP INDEX IF EXISTS idx_language;

CREATE INDEX idx_language ON Programming_language (
    Language
);


-- Index: idx_softbuild
DROP INDEX IF EXISTS idx_softbuild;

CREATE UNIQUE INDEX idx_softbuild ON Software_Build (
    Component_ID,
    Software_ID
);


-- Trigger: trig_delete_build
DROP TRIGGER IF EXISTS trig_delete_build;
CREATE TRIGGER trig_delete_build
        BEFORE DELETE
            ON Component
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (
                           SELECT count(Component_ID) 
                             FROM Software_build swb
                            WHERE old.Component_ID = swb.Component_ID
                       )
=              1) THEN RAISE(ABORT, "Atleaset One Component is needed for the build") END;
END;


-- Trigger: trig_delete_CompStatus_NotReady
DROP TRIGGER IF EXISTS trig_delete_CompStatus_NotReady;
CREATE TRIGGER trig_delete_CompStatus_NotReady
         AFTER DELETE
            ON Inspection
      FOR EACH ROW
          WHEN ( (
                     SELECT FinalScore
                       FROM SoftwareComponent_status_vw
                      WHERE ComponentID = old.Inspected_Component
                 )
< 75) 
BEGIN
    UPDATE Component
       SET Component_Status = 'Not-Ready'
     WHERE Component_ID = (
                              SELECT ComponentID
                                FROM SoftwareComponent_status_vw
                               WHERE ComponentID = old.Inspected_Component
                          );
END;


-- Trigger: trig_delete_CompStatus_Ready
DROP TRIGGER IF EXISTS trig_delete_CompStatus_Ready;
CREATE TRIGGER trig_delete_CompStatus_Ready
         AFTER DELETE
            ON Inspection
      FOR EACH ROW
          WHEN ( (
                     SELECT FinalScore
                       FROM SoftwareComponent_status_vw
                      WHERE ComponentID = old.Inspected_Component
                 )
> 90) 
BEGIN
    UPDATE Component
       SET Component_Status = 'Ready'
     WHERE Component_ID = (
                              SELECT ComponentID
                                FROM SoftwareComponent_status_vw
                               WHERE ComponentID = old.Inspected_Component
                          );
END;


-- Trigger: trig_delete_CompStatus_Usable
DROP TRIGGER IF EXISTS trig_delete_CompStatus_Usable;
CREATE TRIGGER trig_delete_CompStatus_Usable
         AFTER DELETE
            ON Inspection
      FOR EACH ROW
          WHEN (74 < (
                         SELECT FinalScore
                           FROM SoftwareComponent_status_vw
                          WHERE ComponentID = old.Inspected_Component
                     )
< 91) 
BEGIN
    UPDATE Component
       SET Component_Status = 'Usable'
     WHERE Component_ID = (
                              SELECT ComponentID
                                FROM SoftwareComponent_status_vw
                               WHERE ComponentID = old.Inspected_Component
                          );
END;


-- Trigger: trig_delete_employee
DROP TRIGGER IF EXISTS trig_delete_employee;
CREATE TRIGGER trig_delete_employee
        BEFORE DELETE
            ON Employee_exit
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (
                           SELECT UPPER(EmployeeStatus) 
                             FROM Employee
                            WHERE Employee_ID = old.Employee_ID
                       )
=              'EXEMPLOYEE') THEN RAISE(ABORT, "ExEmployee can not be make active again. Create New Record") END;
END;


-- Trigger: trig_delete_EmployeeDelete_ManNotNull
DROP TRIGGER IF EXISTS trig_delete_EmployeeDelete_ManNotNull;
CREATE TRIGGER trig_delete_EmployeeDelete_ManNotNull
        BEFORE DELETE
            ON Employee
      FOR EACH ROW
          WHEN ( (
    SELECT Manager_ID
      FROM Employee
     WHERE Employee_ID = old.Employee_ID
)
IS NOT NULL) 
BEGIN
    UPDATE Employee
       SET Manager_ID = (
               SELECT Manager_ID
                 FROM Employee
                WHERE Employee_ID = old.Employee_ID
           )
     WHERE Manager_ID = old.Employee_ID;
END;


-- Trigger: trig_delete_EmployeeDelete_ManNotNull_Component
DROP TRIGGER IF EXISTS trig_delete_EmployeeDelete_ManNotNull_Component;
CREATE TRIGGER trig_delete_EmployeeDelete_ManNotNull_Component
        BEFORE DELETE
            ON Employee
      FOR EACH ROW
          WHEN ( (
    SELECT Manager_ID
      FROM Employee
     WHERE Employee_ID = old.Employee_ID
)
IS NOT NULL) 
BEGIN
    UPDATE Component
       SET Owner = (
               SELECT Manager_ID
                 FROM Employee
                WHERE Employee_ID = old.Employee_ID
           )
     WHERE Owner = old.Employee_ID;
END;


-- Trigger: trig_delete_EmployeeDelete_ManNull
DROP TRIGGER IF EXISTS trig_delete_EmployeeDelete_ManNull;
CREATE TRIGGER trig_delete_EmployeeDelete_ManNull
        BEFORE DELETE
            ON Employee
      FOR EACH ROW
          WHEN ( (
    SELECT Manager_ID
      FROM Employee
     WHERE Employee_ID = old.Employee_ID
)
IS NULL) 
BEGIN
    UPDATE Employee
       SET Manager_ID = (
               SELECT Employee_ID
                 FROM Employee
                WHERE Hire_date = (
                                      SELECT min(Hire_Date) 
                                        FROM Employee
                                  )
                LIMIT 1
           )
     WHERE Manager_ID = old.Employee_ID;
END;


-- Trigger: trig_delete_EmployeeDelete_ManNull_Component
DROP TRIGGER IF EXISTS trig_delete_EmployeeDelete_ManNull_Component;
CREATE TRIGGER trig_delete_EmployeeDelete_ManNull_Component
        BEFORE DELETE
            ON Employee
      FOR EACH ROW
          WHEN ( (
    SELECT Manager_ID
      FROM Employee
     WHERE Employee_ID = old.Employee_ID
)
IS NULL) 
BEGIN
    UPDATE Component
       SET Owner = (
               SELECT Employee_ID
                 FROM Employee
                WHERE Hire_date = (
                                      SELECT min(Hire_Date) 
                                        FROM Employee
                                  )
                LIMIT 1
           )
     WHERE Owner = old.Employee_ID;
END;


-- Trigger: trig_delete_ProdStatus_NotReady
DROP TRIGGER IF EXISTS trig_delete_ProdStatus_NotReady;
CREATE TRIGGER trig_delete_ProdStatus_NotReady
         AFTER DELETE
            ON Inspection
      FOR EACH ROW
          WHEN ( (
                     SELECT FinalScore
                       FROM SoftwareProduct_status_vw
                      WHERE ComponentID = old.Inspected_Component
                 )
< 75) 
BEGIN
    UPDATE Software_Product
       SET Product_Status = 'Not-Ready'
     WHERE Software_ID = (
                             SELECT SoftwareID
                               FROM SoftwareProduct_status_vw
                              WHERE ComponentID = old.Inspected_Component
                         );
END;


-- Trigger: trig_delete_ProdStatus_Ready
DROP TRIGGER IF EXISTS trig_delete_ProdStatus_Ready;
CREATE TRIGGER trig_delete_ProdStatus_Ready
         AFTER DELETE
            ON Inspection
      FOR EACH ROW
          WHEN ( (
                     SELECT FinalScore
                       FROM SoftwareProduct_status_vw
                      WHERE ComponentID = old.Inspected_Component
                 )
> 90) 
BEGIN
    UPDATE Software_Product
       SET Product_Status = 'Ready'
     WHERE Software_ID = (
                             SELECT SoftwareID
                               FROM SoftwareProduct_status_vw
                              WHERE ComponentID = old.Inspected_Component
                         );
END;


-- Trigger: trig_delete_ProdStatus_Usable
DROP TRIGGER IF EXISTS trig_delete_ProdStatus_Usable;
CREATE TRIGGER trig_delete_ProdStatus_Usable
         AFTER DELETE
            ON Inspection
      FOR EACH ROW
          WHEN (74 < (
                         SELECT FinalScore
                           FROM SoftwareProduct_status_vw
                          WHERE ComponentID = old.Inspected_Component
                     )
< 91) 
BEGIN
    UPDATE Software_Product
       SET Product_Status = 'Usable'
     WHERE Software_ID = (
                             SELECT SoftwareID
                               FROM SoftwareProduct_status_vw
                              WHERE ComponentID = old.Inspected_Component
                         );
END;


-- Trigger: trig_insert_Component_OwnerCheck
DROP TRIGGER IF EXISTS trig_insert_Component_OwnerCheck;
CREATE TRIGGER trig_insert_Component_OwnerCheck
        BEFORE INSERT
            ON Component
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (
                           SELECT UPPER(EmployeeStatus) 
                             FROM Employee
                            WHERE Employee_ID = new.Owner
                       )
=              'EXEMPLOYEE') THEN RAISE(ABORT, "Owner can not be an ExEmployee") END;
END;


-- Trigger: trig_insert_ComponentStatus
DROP TRIGGER IF EXISTS trig_insert_ComponentStatus;
CREATE TRIGGER trig_insert_ComponentStatus
        BEFORE INSERT
            ON Component
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (UPPER(new.Component_Status) NOT IN ('READY', 'NOT-READY', 'USABLE') ) ) THEN RAISE(ABORT, "Wrong type of Component_Status") END;
END;


-- Trigger: trig_insert_CompStatus_NotReady
DROP TRIGGER IF EXISTS trig_insert_CompStatus_NotReady;
CREATE TRIGGER trig_insert_CompStatus_NotReady
         AFTER INSERT
            ON Inspection
      FOR EACH ROW
          WHEN (new.Score < 75) 
BEGIN
    UPDATE Component
       SET Component_Status = 'Not-Ready'
     WHERE Component_ID = new.Inspected_Component;
END;


-- Trigger: trig_insert_CompStatus_Ready
DROP TRIGGER IF EXISTS trig_insert_CompStatus_Ready;
CREATE TRIGGER trig_insert_CompStatus_Ready
         AFTER INSERT
            ON Inspection
      FOR EACH ROW
          WHEN (new.Score > 90) 
BEGIN
    UPDATE Component
       SET Component_Status = 'Ready'
     WHERE Component_ID = new.Inspected_Component;
END;


-- Trigger: trig_insert_CompStatus_Usable
DROP TRIGGER IF EXISTS trig_insert_CompStatus_Usable;
CREATE TRIGGER trig_insert_CompStatus_Usable
         AFTER INSERT
            ON Inspection
      FOR EACH ROW
          WHEN (74 < new.Score < 91) 
BEGIN
    UPDATE Component
       SET Component_Status = 'Usable'
     WHERE Component_ID = new.Inspected_Component;
END;


-- Trigger: trig_insert_Employee_EndDate
DROP TRIGGER IF EXISTS trig_insert_Employee_EndDate;
CREATE TRIGGER trig_insert_Employee_EndDate
        BEFORE INSERT
            ON Employee_exit
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (
                           SELECT UPPER(EmployeeStatus) 
                             FROM Employee
                            WHERE Employee_ID = new.Employee_ID
                       )
=              'ACTIVEEMPLOYEE') THEN RAISE(ABORT, "End date cannot be Assigned for ActiveEmployee") END;
END;


-- Trigger: trig_insert_EmployeeEndDate
DROP TRIGGER IF EXISTS trig_insert_EmployeeEndDate;
CREATE TRIGGER trig_insert_EmployeeEndDate
         AFTER INSERT
            ON Employee
      FOR EACH ROW
          WHEN (
                   SELECT UPPER(EmployeeStatus) 
                     FROM Employee
                    WHERE Employee_ID = new.Employee_ID
               )
= 'EXEMPLOYEE'
BEGIN
    INSERT INTO Employee_exit VALUES (
                                  new.Employee_ID,
                                  date('now', 'localtime') 
                              );
END;


-- Trigger: trig_insert_EmpStatus
DROP TRIGGER IF EXISTS trig_insert_EmpStatus;
CREATE TRIGGER trig_insert_EmpStatus
        BEFORE INSERT
            ON Employee
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (UPPER(new.EmployeeStatus) NOT IN ('ACTIVEEMPLOYEE', 'EXEMPLOYEE') ) ) THEN RAISE(ABORT, "Wrong type of Employee Status") END;
END;


-- Trigger: trig_insert_ExEmployeeASManager
DROP TRIGGER IF EXISTS trig_insert_ExEmployeeASManager;
CREATE TRIGGER trig_insert_ExEmployeeASManager
        BEFORE INSERT
            ON Employee
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (
                           SELECT UPPER(EmployeeStatus) 
                             FROM Employee
                            WHERE Employee_ID = new.manager_ID
                       )
=              'EXEMPLOYEE') THEN RAISE(ABORT, "Manager can not be an ExEmployee") END;
END;


-- Trigger: trig_insert_hire_date
DROP TRIGGER IF EXISTS trig_insert_hire_date;
CREATE TRIGGER trig_insert_hire_date
        BEFORE INSERT
            ON Employee
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (julianday(new.Hire_date) - julianday(date('now', 'localtime') ) ) IS NULL) THEN RAISE(ABORT, "Insert Date is in a Wrong Format. Use YYYY-MM-DD Somya") END;
END;


-- Trigger: trig_insert_hire_date_in_past
DROP TRIGGER IF EXISTS trig_insert_hire_date_in_past;
CREATE TRIGGER trig_insert_hire_date_in_past
        BEFORE INSERT
            ON Employee
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (julianday(new.Hire_date) - julianday(date('now', 'localtime') ) ) > 0) THEN RAISE(ABORT, "Hire date must be in the past") END;
END;


-- Trigger: trig_insert_Inspection_OwnerCheck
DROP TRIGGER IF EXISTS trig_insert_Inspection_OwnerCheck;
CREATE TRIGGER trig_insert_Inspection_OwnerCheck
        BEFORE INSERT
            ON Inspection
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (
                           SELECT UPPER(EmployeeStatus) 
                             FROM Employee
                            WHERE Employee_ID = new.Own_BY
                       )
=              'EXEMPLOYEE') THEN RAISE(ABORT, "Owner can not be an ExEmployee") END;
END;


-- Trigger: trig_insert_ProdStatus_NotReady
DROP TRIGGER IF EXISTS trig_insert_ProdStatus_NotReady;
CREATE TRIGGER trig_insert_ProdStatus_NotReady
         AFTER INSERT
            ON Inspection
      FOR EACH ROW
          WHEN ( (
                     SELECT FinalScore
                       FROM SoftwareProduct_status_vw
                      WHERE ComponentID = new.Inspected_Component
                 )
< 75) 
BEGIN
    UPDATE Software_Product
       SET Product_Status = 'Not-Ready'
     WHERE Software_ID = (
                             SELECT SoftwareID
                               FROM SoftwareProduct_status_vw
                              WHERE ComponentID = new.Inspected_Component
                         );
END;


-- Trigger: trig_insert_ProdStatus_Ready
DROP TRIGGER IF EXISTS trig_insert_ProdStatus_Ready;
CREATE TRIGGER trig_insert_ProdStatus_Ready
         AFTER INSERT
            ON Inspection
      FOR EACH ROW
          WHEN ( (
                     SELECT FinalScore
                       FROM SoftwareProduct_status_vw
                      WHERE ComponentID = new.Inspected_Component
                 )
> 90) 
BEGIN
    UPDATE Software_Product
       SET Product_Status = 'Ready'
     WHERE Software_ID = (
                             SELECT SoftwareID
                               FROM SoftwareProduct_status_vw
                              WHERE ComponentID = new.Inspected_Component
                         );
END;


-- Trigger: trig_insert_ProdStatus_Usable
DROP TRIGGER IF EXISTS trig_insert_ProdStatus_Usable;
CREATE TRIGGER trig_insert_ProdStatus_Usable
         AFTER INSERT
            ON Inspection
      FOR EACH ROW
          WHEN (74 < (
                         SELECT FinalScore
                           FROM SoftwareProduct_status_vw
                          WHERE ComponentID = new.Inspected_Component
                     )
< 91) 
BEGIN
    UPDATE Software_Product
       SET Product_Status = 'Usable'
     WHERE Software_ID = (
                             SELECT SoftwareID
                               FROM SoftwareProduct_status_vw
                              WHERE ComponentID = new.Inspected_Component
                         );
END;


-- Trigger: trig_insert_ProductStatus
DROP TRIGGER IF EXISTS trig_insert_ProductStatus;
CREATE TRIGGER trig_insert_ProductStatus
        BEFORE INSERT
            ON Software_Product
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (UPPER(new.Product_Status) NOT IN ('READY', 'NOT-READY', 'USABLE') ) ) THEN RAISE(ABORT, "Wrong type of Product_Status") END;
END;


-- Trigger: trig_insert_SoftwareProduct
DROP TRIGGER IF EXISTS trig_insert_SoftwareProduct;
CREATE TRIGGER trig_insert_SoftwareProduct
         AFTER INSERT
            ON Software_Product
      FOR EACH ROW
BEGIN
    INSERT INTO Software_Build (
                                   Software_ID,
                                   Component_ID
                               )
                               VALUES (
                                   new.software_ID,
                                   (
                                       SELECT Component_ID
                                         FROM Component
                                        ORDER BY Component_ID ASC
                                        LIMIT 1
                                   )
                               );
END;


-- Trigger: trig_Update_Cmpnt_EmployeeExit_ManNotNull
DROP TRIGGER IF EXISTS trig_Update_Cmpnt_EmployeeExit_ManNotNull;
CREATE TRIGGER trig_Update_Cmpnt_EmployeeExit_ManNotNull
        BEFORE UPDATE OF EmployeeStatus
            ON Employee
      FOR EACH ROW
          WHEN ( (
    SELECT Manager_ID
      FROM Employee
     WHERE Employee_ID = new.Employee_ID AND 
           UPPER(new.EmployeeStatus) = 'EXEMPLOYEE'
)
IS NOT NULL) 
BEGIN
    UPDATE Component
       SET Owner = (
               SELECT Manager_ID
                 FROM Employee
                WHERE Employee_ID = new.Employee_ID
           )
     WHERE Owner = new.Employee_ID;
END;


-- Trigger: trig_Update_Cmpnt_EmployeeExit_ManNull
DROP TRIGGER IF EXISTS trig_Update_Cmpnt_EmployeeExit_ManNull;
CREATE TRIGGER trig_Update_Cmpnt_EmployeeExit_ManNull
        BEFORE UPDATE OF EmployeeStatus
            ON Employee
      FOR EACH ROW
          WHEN ( (
    SELECT Manager_ID
      FROM Employee
     WHERE Employee_ID = new.Employee_ID AND 
           UPPER(EmployeeStatus) = 'EXEMPLOYEE'
)
IS NULL) 
BEGIN
    UPDATE Component
       SET Owner = (
               SELECT Employee_ID
                 FROM Employee
                WHERE Hire_date = (
                                      SELECT min(Hire_Date) 
                                        FROM Employee
                                  )
                LIMIT 1
           )
     WHERE Owner = new.Employee_ID;
END;


-- Trigger: trig_update_Component_OwnerCheck
DROP TRIGGER IF EXISTS trig_update_Component_OwnerCheck;
CREATE TRIGGER trig_update_Component_OwnerCheck
        BEFORE UPDATE OF Owner
            ON Component
BEGIN
    SELECT CASE WHEN ( (
                           SELECT UPPER(EmployeeStatus) 
                             FROM Employee
                            WHERE Employee_ID = new.Owner
                       )
=              'EXEMPLOYEE') THEN RAISE(ABORT, "Owner can not be an ExEmployee") END;
END;


-- Trigger: trig_update_ComponentStatus
DROP TRIGGER IF EXISTS trig_update_ComponentStatus;
CREATE TRIGGER trig_update_ComponentStatus
        BEFORE UPDATE OF Component_Status
            ON Component
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (UPPER(new.Component_Status) NOT IN ('READY', 'NOT-READY', 'USABLE') ) ) THEN RAISE(ABORT, "Wrong type of Component_Status") END;
END;


-- Trigger: trig_update_Employee_EndDate
DROP TRIGGER IF EXISTS trig_update_Employee_EndDate;
CREATE TRIGGER trig_update_Employee_EndDate
        BEFORE UPDATE OF End_date
            ON Employee_exit
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (
                           SELECT UPPER(EmployeeStatus) 
                             FROM Employee
                            WHERE Employee_ID = new.Employee_ID
                       )
=              'ACTIVEEMPLOYEE') THEN RAISE(ABORT, "End date cannot be Assigned for ActiveEmployee") END;
END;


-- Trigger: trig_update_EmployeeEndDate
DROP TRIGGER IF EXISTS trig_update_EmployeeEndDate;
CREATE TRIGGER trig_update_EmployeeEndDate
         AFTER UPDATE OF EmployeeStatus
            ON Employee
      FOR EACH ROW
          WHEN (
                   SELECT UPPER(EmployeeStatus) 
                     FROM Employee
                    WHERE Employee_ID = new.Employee_ID
               )
= 'EXEMPLOYEE'
BEGIN
    INSERT INTO Employee_exit VALUES (
                                  new.Employee_ID,
                                  date('now', 'localtime') 
                              );
END;


-- Trigger: trig_Update_EmployeeExit_ManNotNull
DROP TRIGGER IF EXISTS trig_Update_EmployeeExit_ManNotNull;
CREATE TRIGGER trig_Update_EmployeeExit_ManNotNull
        BEFORE UPDATE OF EmployeeStatus
            ON Employee
      FOR EACH ROW
          WHEN ( (
    SELECT Manager_ID
      FROM Employee
     WHERE Employee_ID = new.Employee_ID AND 
           UPPER(new.EmployeeStatus) = 'EXEMPLOYEE'
)
IS NOT NULL) 
BEGIN
    UPDATE Employee
       SET Manager_ID = (
               SELECT Manager_ID
                 FROM Employee
                WHERE Employee_ID = new.Employee_ID
           )
     WHERE Manager_ID = new.Employee_ID;
END;


-- Trigger: trig_Update_EmployeeExit_ManNull
DROP TRIGGER IF EXISTS trig_Update_EmployeeExit_ManNull;
CREATE TRIGGER trig_Update_EmployeeExit_ManNull
        BEFORE UPDATE OF EmployeeStatus
            ON Employee
      FOR EACH ROW
          WHEN ( (
    SELECT Manager_ID
      FROM Employee
     WHERE Employee_ID = new.Employee_ID AND 
           UPPER(EmployeeStatus) = 'EXEMPLOYEE'
)
IS NULL) 
BEGIN
    UPDATE Employee
       SET Manager_ID = (
               SELECT Employee_ID
                 FROM Employee
                WHERE Hire_date = (
                                      SELECT min(Hire_Date) 
                                        FROM Employee
                                  )
                LIMIT 1
           )
     WHERE Manager_ID = new.Employee_ID;
END;


-- Trigger: trig_update_EmployeeStatus
DROP TRIGGER IF EXISTS trig_update_EmployeeStatus;
CREATE TRIGGER trig_update_EmployeeStatus
        BEFORE UPDATE OF EmployeeStatus
            ON Employee
      FOR EACH ROW
          WHEN (
                   SELECT UPPER(EmployeeStatus) 
                     FROM Employee
                    WHERE Employee_ID = new.Employee_ID
               )
= 'EXEMPLOYEE'
BEGIN
    DELETE FROM Employee_exit
          WHERE Employee_ID = new.Employee_ID;
END;


-- Trigger: trig_update_EmpStatus
DROP TRIGGER IF EXISTS trig_update_EmpStatus;
CREATE TRIGGER trig_update_EmpStatus
        BEFORE UPDATE OF EmployeeStatus
            ON Employee
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (UPPER(new.EmployeeStatus) NOT IN ('ACTIVEEMPLOYEE', 'EXEMPLOYEE') ) ) THEN RAISE(ABORT, "Wrong type of Employee Status") END;
END;


-- Trigger: trig_update_end_date_in_future
DROP TRIGGER IF EXISTS trig_update_end_date_in_future;
CREATE TRIGGER trig_update_end_date_in_future
        BEFORE UPDATE
            ON Employee_exit
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (julianday(new.Exit_date) - julianday(date('now', 'localtime') ) ) > 0) THEN RAISE(ABORT, "End date must be present or past") END;
END;


-- Trigger: trig_update_end_date_invalid
DROP TRIGGER IF EXISTS trig_update_end_date_invalid;
CREATE TRIGGER trig_update_end_date_invalid
        BEFORE UPDATE OF End_date
            ON Employee_exit
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (julianday(new.Exit_date) - julianday(date('now', 'localtime') ) ) IS NULL) THEN RAISE(ABORT, "Updated Date is in a Wrong Format. Use YYYY-MM-DD") END;
END;


-- Trigger: trig_update_ExEmployeeASManager
DROP TRIGGER IF EXISTS trig_update_ExEmployeeASManager;
CREATE TRIGGER trig_update_ExEmployeeASManager
        BEFORE UPDATE
            ON Employee
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (
                           SELECT UPPER(EmployeeStatus) 
                             FROM Employee
                            WHERE Employee_ID = new.manager_ID
                       )
=              'EXEMPLOYEE') THEN RAISE(ABORT, "Manager can not be an ExEmployee") END;
END;


-- Trigger: trig_update_hire_date
DROP TRIGGER IF EXISTS trig_update_hire_date;
CREATE TRIGGER trig_update_hire_date
        BEFORE UPDATE OF Hire_date
            ON Employee
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (julianday(new.Hire_date) - julianday(date('now', 'localtime') ) ) IS NULL) THEN RAISE(ABORT, "Updated Date is in a Wrong Format. Use YYYY-MM-DD") END;
END;


-- Trigger: trig_update_hire_date_in_past
DROP TRIGGER IF EXISTS trig_update_hire_date_in_past;
CREATE TRIGGER trig_update_hire_date_in_past
        BEFORE UPDATE
            ON Employee
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (julianday(new.Hire_date) - julianday(date('now', 'localtime') ) ) > 0) THEN RAISE(ABORT, "Hire date must be in the past") END;
END;


-- Trigger: trig_update_Inspection
DROP TRIGGER IF EXISTS trig_update_Inspection;
CREATE TRIGGER trig_update_Inspection
        BEFORE UPDATE OF Inspected_Component,
                         Inspection_date,
                         Own_BY,
                         Score
            ON Inspection
      FOR EACH ROW
BEGIN
    SELECT RAISE(ABORT, "Can Not Update the Column Value") "END";
END;


-- Trigger: trig_update_ProductStatus
DROP TRIGGER IF EXISTS trig_update_ProductStatus;
CREATE TRIGGER trig_update_ProductStatus
        BEFORE UPDATE OF Product_Status
            ON Software_Product
      FOR EACH ROW
BEGIN
    SELECT CASE WHEN ( (UPPER(new.Product_Status) NOT IN ('READY', 'NOT-READY', 'USABLE') ) ) THEN RAISE(ABORT, "Wrong type of Product_Status") END;
END;


-- View: Employee_Seniority_vw
DROP VIEW IF EXISTS Employee_Seniority_vw;
CREATE VIEW Employee_Seniority_vw AS
    SELECT Employee_ID,
           Employee_Name,
           CASE WHEN ( (julianday(date('now') ) - (julianday(Hire_date) ) ) < 365) THEN 'Newbie' WHEN ( (julianday(date('now') ) - (julianday(Hire_date) ) ) > 1825) THEN 'Senior' WHEN (365 <= (julianday(date('now') ) - (julianday(Hire_date) ) ) <= 1825) THEN 'Junior' END Seniority
      FROM Employee E
     WHERE UPPER(E.EmployeeStatus) = 'ACTIVEEMPLOYEE';


-- View: SoftwareComponent_status_vw
DROP VIEW IF EXISTS SoftwareComponent_status_vw;
CREATE VIEW SoftwareComponent_status_vw AS
    SELECT comp.Component_ID AS ComponentID,
           MIN(ifNULL(insp.Score, 0) ) AS FinalScore
      FROM Component comp
           LEFT JOIN
           Inspection insp ON (insp.Inspected_Component = comp.Component_ID AND 
                               insp.Inspection_date IN (
                                  SELECT MAX(Inspection_date) 
                                    FROM Inspection
                                   GROUP BY Inspected_Component
                              )
                              ) 
     GROUP BY comp.Component_ID
     ORDER BY comp.Component_ID;


-- View: SoftwareProduct_status_vw
DROP VIEW IF EXISTS SoftwareProduct_status_vw;
CREATE VIEW SoftwareProduct_status_vw AS
    SELECT swb.Software_ID AS SoftwareID,
           swb.Component_ID AS ComponentID,
           MIN(ifNULL(insp.Score, 0) ) AS FinalScore
      FROM Component comp
           JOIN
           Software_Build swb ON swb.Component_ID = comp.Component_ID
           LEFT JOIN
           Inspection insp ON (insp.Inspected_Component = comp.Component_ID AND 
                               insp.Inspection_date IN (
                                  SELECT MAX(Inspection_date) 
                                    FROM Inspection
                                   GROUP BY Inspected_Component
                              )
                              ) 
     GROUP BY swb.Software_ID
     ORDER BY swb.Software_ID;


COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
