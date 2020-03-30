import mysql.connector


def database_connector():
    """
    De functie database_connector maakt een connectie met de database aan
    :return: de connectie
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    return conn.cursor()


def database_collect(table):
    """
    De functie database_collect haalt alle informatie op uit de desbetreffende
    table
    :param table: De table waaruit alles opgehaald moet worden.
    ":return None
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute(f"""select * from {table}""")
    rows = cursor.fetchall()
    for x in rows:
        print(x)


def database_update(table, info):
    """
    De functie database_update insert informatie in een desbetreffende table
    :param table: De table waarin de informatie geinsert moet worden
    :param info: De informatie
    :return: None
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    new_info = ", ".join(repr(e) for e in info)
    cursor.execute(f"""insert into {table} value ({new_info});""")
    conn.commit()


def database_organism_checker(organism):
    """
    De functie database_organism_checker checkt in de table "organisme" of deze
    bestaat
    :param organism: Het organisme
    :return: Het organisme_ID wat de PK in de table is.
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute(f"select Organisme_ID from organisme where Organisme_Naam "
                   f"like '{organism}';")
    try:
        print('hi')
        return cursor.fetchall()[0][0]
    except IndexError:
        cursor.execute("select max(Organisme_ID) from organisme;")
        new_id = cursor.fetchall()[0][0] + 1
        info = [new_id, organism]
        new_info = ", ".join(repr(e) for e in info)
        cursor.execute(f"""insert into organisme value ({new_info});""")
        conn.commit()
        return new_id


def database_orf_checker(orf):
    """
    De database_orf_checker haalt de ORF_ID op uit de table orfs
    :param orf: Het stuk DNA sequentie wat als ORF gevonden is
    :return: de ORF_ID
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute(f"select ORFs_ID from orfs where ORFs like '{orf}';")
    return cursor.fetchall()[0][0]


def database_blastid_checker():
    """
    De functie database_blastID_checker haalt de blast_ID op uit de table
    blastresultsorf
    :return: de BLAST_ID
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute("select max(BLAST_ID) from blastresultsorf;")
    return cursor.fetchall()[0][0]
