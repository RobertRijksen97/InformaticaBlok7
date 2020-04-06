# Naam: Robert Rijksen
# Functie: diverse functies voor de orf database
# Datum: 06-04-2020

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
    ":return De string_builder, hierin staat alles in de juiste format om te
            visualiseren in de gui table
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute(f"""select * from {table} join dna_data dd on 
                       orfs.DNA_seq_ID = dd.DNA_seq_ID;""")
    rows = cursor.fetchall()
    string_builder = ""
    for x in rows:
        new_string = '\t'.join(str(b) for b in x)
        string_builder += new_string + "\n"

    return string_builder


def database_dna_collect(id):
    """
    De functie database_dna_collect haalt de DNA sequentie op uit dna table
    :param id: de id die opgehaald moet worden
    ":return de DNA sequentie
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    # cursor.execute(f"""select * from {table}""")
    cursor.execute(f"""select DNA_sequentie from dna_data where DNA_seq_ID 
                       like {id};""")
    return cursor.fetchall()[0][0]


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


def database_dna_data_id_checker():
    """
    De functie database_dna_data haalt de hoogste DNA_seq_id op
    :return: de hoogste DNA_seq_id
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute("select max(DNA_seq_ID) from dna_data;")
    return cursor.fetchall()[0][0]


def database_orf_id_checker():
    """
    De functie database_orf_id_checker haalt de laatste orf_id op uit de table
    orfs
    :return:de laatste orf_id
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute(f"select max(ORFs_ID) from orfs;")
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


def database_delete(table, sort_id, number_id):
    """
    Deze functie database_delete heeft als functie om een row uit de database
    te verwijderen
    :param sort_id: Welk soort ID je wilt verwijderen, dus bijvoorbeeld
           blast_id of een orf_id
    :param number_id: Het ID in int vorm.
    :return: None
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute(f"delete from {table} where {sort_id} like "
                   f"{number_id};")
    conn.commit()


def string_to_list_converter(string):
    """
    Deze functie string_to_list_converter, maakt de string die gescheiden is
    met een tab en bij het eind van de string een enter, weer om naar een list.
    Dit is gedaan om het opslaan van de resultaten makkelijker te maken
    :param string: De string met alle resultaten
    :return: De list
    """
    results_list = []
    string_list = string.replace("\n", ",").split(",")
    for i in range(len(string_list)):
        new_i = string_list[i].replace("\t", ",").split(",")
        results_list.append([])
        for x in range(len(new_i)):
            try:
                results_list[i].append(int(new_i[x]))
            except ValueError:
                results_list[i].append(new_i[x])
    return results_list
