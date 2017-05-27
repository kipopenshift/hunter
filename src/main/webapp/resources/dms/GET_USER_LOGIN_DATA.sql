create or replace function GET_USER_LOGIN_DATA(user_name IN HNTR_USR.usr_nam%type) RETURN VARCHAR AS

USR_DATA_XML XMLTYPE := XMLTYPE( '<loginData passCode="null" user_name="'|| user_name ||'" active="N" ><roles></roles></loginData>' );
pass_code HNTR_USR.PSSWRD%type := null;
active    HNTR_USR.ACTIV%type := null;
USR_ID   HNTR_USR.usr_nam%type := 0;

BEGIN 

select u.PSSWRD, u.ACTIV,u.USR_ID INTO pass_code,active,USR_ID FROM HNTR_USR u WHERE u.USR_NAM = user_name AND u.ACTIV = 'Y';

SELECT UPDATEXML(USR_DATA_XML, '/loginData/@active',active  ) INTO USR_DATA_XML FROM DUAL;
SELECT UPDATEXML(USR_DATA_XML, '/loginData/@passCode',pass_code  ) INTO USR_DATA_XML FROM DUAL;

FOR rec IN ( 
  SELECT
    u.USR_ID,
    u.USR_NAM,
    r.RL_NAM
  FROM
    HNTR_USR_RLS us,
    HNTR_USR u,
    USER_ROLE r
  WHERE us.USR_ID   = u.USR_ID
  AND u.USR_NAM = user_name
  AND r.ROLE_ID = us.ROLE_ID
) LOOP
  SELECT APPENDCHILDXML( USR_DATA_XML, '/loginData/roles', XMLTYPE('<role>'|| rec.RL_NAM ||'</role>') ) INTO USR_DATA_XML FROM DUAL;
END LOOP;


return USR_DATA_XML.getClobVal();

EXCEPTION WHEN NO_DATA_FOUND THEN RETURN NULL;

END;