from Bio.Blast import NCBIWWW
from Bio.Blast import NCBIXML
from Bio import SearchIO
import re
import database_orf

import pickle
import time


def main():
    sequentie = "MPPRLKTSMKYKENRLKSFEEGVKLPGGKRKVYWFDNNDEKIIEPLIECGFYYAPVKSNFSQIICSFCGQPETIEDNLDIKVLLEDHYNKNNNCSLSLIMLSGLENNKKSSKETSFNYWSNHKVNVLKDPLSKESFKFRLNFFNKKYPLDKLRNFKLNSKKLSESGFIYSPFFENDDRVSCYYCNCSLEGWEEYDDPIEEHKKNHNLYCYFLDVYSQKYDGKANAESEIKKLNNDDNIKEDIKRNGKTDHFGRLSEGKEESEGEEDENEPERIEEQKEVKEKETNNMIKDSPIISSNKKPVASVQSESVSAKKTDKDNSEIQNKSLLDATNKNSVKNNSSRSIVYIDDDVALNSNDDEVSFDDLSDFDDKIYSQEQEESSPEPSQPIRRSKRLTKIRAQKDPNDDYWNKLPDRDLYNELMGKKSKSKDNISDSKSESEFEFYDDNLSEEEILETDDASFEPDPPTPPRAINSRLRPAKKQTKNAKEDTDSPDELSPLPSNGSSYSDLITKIEPGASAGISSKLNRKQNKKKIKETIRQAVTDAATESEPVASSYSSPTKQNPAQKESSSIVLSKTPSLSPLQNINDTNDIQKKRLSANELSTGIELDLKSPRKFKKIKLVNKGYSPSPPVYDISDQNLGDYDESNLKFLENNIKPVNIDSLFLKTAKGPALKNDKDIEIDKYQPKSSPIKLNPSNTLTSGLKSKAKSKIIKPLPHSKQSKQKYNILDMSFDDEAFVVSSNKFISIQKRNNEIVPVSTRKKSISEKIPKSTILDNERDQSKVSPPKEQGSSKLDNACIKEDMDKHYSKKNNHPNKRNNFNNDNGHFSIDDSLPSDVSGSTDIESVISQFKKETSLKNYVQIESIKDVASDINKDGSDEEQREVEGAKNDDVRYGAVDELNSISNKEEVEDKFQHNVSRLQRDNDDVESSGVDKGKLVHVSNVQTNPESPTLSDNATQNKGESTITPAISDEKVNQINKNDDDTFDISNSPSIYADYIQDIKEINNEILKSLEIFTNDEDSEGMDEHDNDLKKGEQDSNSNGLSNQDHIGDALDEELLDLANPNANINTSVQLSKDSTNRSIKQIEVDKAVNINYGAEIKSDREKSIQKEEIYKGENHQEQHIIQSSEDNIKETYYLKKDADLTNGGRSSHEDNKELSNYQTSNFKIQDSLPQNGDFINLDKSRHVDSSDNRSSEISLAEGEKLNGEENEIEGSTIDQPNKDINSADNKRSSRILNTETKLNQDISDPDAKDEARKEDDITNVEKSLVKEDSAVAVIRVDEDSNPEIKATDMIYKSKFNGNEVLDQEEKSLAKRDPSNDNVISITDNAIIKSFSKEDKQEIITTRKSKSPNAGITDLSGNFNALLEASTPERREDNPVISRAGKVPSENIDWVPISLSSLAETLHNFEDTANYLKTVATSENDLHNDYDSELTNFISAMPENEEYMTIQEWVKYSASNCGKLVKETCNDIIRVYELDYFRALNVLESLPTED"  # Huidige sequentie
    header = "ORF 1"
    BLASTp(sequentie, header)



def bestandlezer(bestandsnaam):
    """Deze functie maakt 2D lijsten van de sequenties en de headers,
     die later worden gebruikt om een key te maken en
     over de sequenties wordt geitereerd en geblast.
    :return: 2D lijsten headers en sequenties
    """
    bestand = open(bestandsnaam, "r")
    headers = []
    sequenties = []
    for line in bestand:
        lines = line.split('\t')
        headers.append(lines[0])
        sequenties.append(lines[1])
        headers.append(lines[3])
        sequenties.append(lines[4])

    return headers, sequenties


def BLASTp(sequentie, header):
    """Deze functie BLAST de sequenties via BLASTx. Het print de dict en het
    schrijft de alignments weg naar een bestand m.b.v. Pickle.
    :param sequentie: Dit is de sequentie die geblast moet worden.
    :param header: Dit is de header van de sequentie. Deze wordt gebruikt
    in de dict om als key te werken.
    """
    print('-' * 100)

    scientific_name_list_blastx = []

    print('*BLAST1* ' * 3)  # Dit geeft aan bij welke BLAST hij is

    # Dit zijn de gegevens voor het BLASTen.
    result_handle = NCBIWWW.qblast("blastp", "nr", sequentie,
                                   matrix_name="BLOSUM62",
                                   hitlist_size=20)
    # Dit leest de resultaten in
    read = SearchIO.parse(result_handle, 'blast-xml')
    list_result_all = []
    list_results = []
    orf_id = database_orf.database_orf_checker('AT')
    blast_id = database_orf.database_blastid_checker()
    for i in read:
        for hit in i:
            list_results.append('AT')
            list_results.append(hit.description)
            list_results.append(hit[0].evalue)
            list_results.append(orf_id)
            list_results.append(blast_id+1)
            scientific_name = re.search("\[([A-Z][a-z]+\s[a-z]+)", hit.description)
            if scientific_name:
                organism_name = scientific_name.group().replace("[", "").replace("]", "")
                print(organism_name)
                organism_id = database_orf.database_organism_checker(organism_name)
                list_results.append(organism_id)
                scientific_name_list_blastx.append(scientific_name.group().replace("[", "").replace("]", ""))
            else:
                organism_name = re.search("(?<=\[).+?(?=\])", hit.description).group()
                organism_id = database_orf.database_organism_checker(organism_name)
                list_results.append(organism_id)
                scientific_name_list_blastx.append(scientific_name)
            list_result_all.append(list_results)

    print(list_result_all)


main()
