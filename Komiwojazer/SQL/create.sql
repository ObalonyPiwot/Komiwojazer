drop table zestaw;
drop table punkt;
drop table sciezka;
drop table kolejnosc;
drop sequence zestawyID;
drop sequence punktyID;
drop sequence sciezkaID;

create table zestaw(
    ID numeric primary key,
    nazwa NVARCHAR2(30),
    data DATE
);
create table punkt(
    ID numeric primary key,
    IDZestawu numeric,
    coordX float,
    coordY float,
    czyStartowy numeric
);
create table sciezka(
    ID numeric primary key,
    IDZestawu numeric
);
create table kolejnosc(
    IDSciezki numeric,
    IDPunktu numeric,
    NrOdwiedzin numeric
);
  Create sequence zestawyID
minvalue 0
start with 0
increment by 1;
  Create sequence punktyID
minvalue 0
start with 0
increment by 1;
  Create sequence sciezkaID
minvalue 0
start with 0
increment by 1;