# Naam: Robert Rijksen
# Functie: diverse functies voor de orf database
# Datum: 06-04-2020

import mysql.connector
import sys

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
    cursor.execute("""select * from {}""".format(table))
    # cursor.execute("""select * from {} join dna_data dd on orfs.DNA_seq_ID = dd.DNA_seq_ID;""".format(table))
    rows = cursor.fetchall()
    string_builder = ""
    for x in rows:
        new_string = '\t'.join(str(b) for b in x)
        string_builder += new_string + "\n"

    print(string_builder)


def database_dna_collect(dna_id):
    """
    De functie database_dna_collect haalt de DNA sequentie op uit dna table
    :param dna_id: de id die opgehaald moet worden
    ":return de DNA sequentie
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    # cursor.execute(f"""select * from {table}""")
    cursor.execute("""select DNA_sequentie from dna_data where DNA_seq_ID 
                       like {};""".format(dna_id))
    info = cursor.fetchall()[0][0]
    print(info)
    return info

def database_dna_data_id_checker():
    """
    De functie database_dna_data
    :return:
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute("select max(DNA_seq_ID) from dna_data;")
    latest_id = cursor.fetchall()[0][0]
    print(latest_id)
    return latest_id


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
    # inf = info.replace("\t", ", ")
    info = info.split("\t")
    new_info = ", ".join(repr(e) for e in info)
    cursor.execute("""insert into {} value ({});""".format(table, new_info))
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
    cursor.execute("select Organisme_ID from organisme where Organisme_Naam "
                   "like '{}';".format(organism))
    try:
        id = cursor.fetchall()[0][0]
        print(id)
        return id
    except IndexError:
        cursor.execute("select max(Organisme_ID) from organisme;")
        new_id = cursor.fetchall()[0][0] + 1
        info = [new_id, organism]
        new_info = ", ".join(repr(e) for e in info)
        cursor.execute("""insert into organisme value ({});""".format(new_info))
        conn.commit()
        print(new_id)
        return new_id


def database_organism_name_checker(org_id):
    """
    De functie database_organism_checker checkt in de table "organisme" welk
    organisme bij een gegeven organisme id hoort
    :param orgid: Het organisme id
    :return: Het organisme_ID wat de PK in de table is.
    """
    conn = mysql.connector.connect(
        host="hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
        user="rohtv@hannl-hlo-bioinformatica-mysqlsrv",
        db="rohtv", password='pwd123')
    cursor = conn.cursor()
    cursor.execute("select Organisme_Naam from organisme where Organisme_ID "
               "like '{}';".format(org_id))
    organism = cursor.fetchall()[0][0]
    print(organism)
    return organism


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
    cursor.execute("select ORFs_ID from orfs where ORFs like '{}';".format(orf))
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
    info = cursor.fetchall()[0][0]
    print(info)
    return info


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
    cursor.execute("delete from {} where {} like {};".format(table, sort_id, number_id))
    conn.commit()


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
    cursor.execute("select max(ORFs_ID) from orfs;")
    print(cursor.fetchall()[0][0])


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


def main():
    if sys.argv[1] == "collect":
        database_collect(sys.argv[2])
    elif sys.argv[1] == "save":
        database_update(sys.argv[2], sys.argv[3])
    elif sys.argv[1] == "delete":
        database_delete(sys.argv[2], sys.argv[3])
    elif sys.argv[1] == "latestID":
        database_orf_id_checker()
    elif sys.argv[1] == "latestDna":
        database_dna_data_id_checker()
    elif sys.argv[1] == "checkOrg":
        database_organism_checker(sys.argv[2])
    elif sys.argv[1] == "collect_dna":
        database_dna_collect(sys.argv[2])
    elif sys.argv[1] == "latestBlast":
        database_blastid_checker()
    elif sys.argv[1] == "getOrgName":
        database_organism_name_checker(sys.argv[2])


main()
