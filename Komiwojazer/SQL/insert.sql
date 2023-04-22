INSERT INTO zestaw VALUES(zestawyID.nextval, 'Imie',SYSDATE);
INSERT INTO zestaw VALUES(zestawyID.nextval, 'Imie1',SYSDATE);
INSERT INTO zestaw VALUES(zestawyID.nextval, 'Imie2',SYSDATE);

INSERT INTO punkt VALUES(punktyID.nextval,zestawyID.currval, 0.5,0.5, 0);

select zestawyID.nextval from dual;