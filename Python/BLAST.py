from Bio.Blast import NCBIWWW
from Bio import SearchIO
import re
import sys
import database_orf


def main():
    BLAST()


def BLAST():
    """Deze functie BLAST de sequenties via BLASTx. Het print de dict en het
    schrijft de alignments weg naar een bestand m.b.v. Pickle.
    :param sequentie: Dit is de sequentie die geblast moet worden.
    :param header: Dit is de header van de sequentie. Deze wordt gebruikt
    in de dict om als key te werken.
    """

    scientific_name_list_blastx = []

    # Dit zijn de gegevens voor het BLASTen.
    result_handle = NCBIWWW.qblast("{}".format(sys.argv[1]), "{}".format(sys.argv[2]), "{}".format(sys.argv[3]),
                                   hitlist_size=20)
    # Dit leest de resultaten in
    read = SearchIO.parse(result_handle, 'blast-xml')
    list_result_all = []
    # orf_id = database_orf.database_orf_checker(sys.argv[3])
    string_builder_table = ""
    for i in read:
        for hit in i:
            # blast_id = database_orf.database_blastid_checker()
            scientific_name = re.search("\[([A-Z][a-z ]+\s.+\])", hit.description)
            string_builder_table += str(hit.accession) + '\t' \
                                    + str(hit.description) + '\t' \
                                    + str(hit[0].evalue) + '\t' \
                                    + str(hit[0].ident_num) + '\t' \
                                    + str(sys.argv[4]) + '\t'# + str(blast_id + 1) + '\t'
            if scientific_name:
                organism_name = scientific_name.group().replace("[", "") \
                    .replace("]", "")
                organism_id = database_orf.database_organism_checker(organism_name, "0")
                string_builder_table += (str(organism_id) + '\n')
            else:
                string_builder_table += '1\n'

    print(string_builder_table)


main()
