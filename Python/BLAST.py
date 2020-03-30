from Bio.Blast import NCBIWWW
from Bio import SearchIO
import re
import database_orf


def main():
    sequentie = "MPPRLKTSMKYKENRLKSFEEGVKLPGGKRKVYWFDNNDEKIIEPLIECGFYYAPVKSNFSQIICSFCGQPETIEDNLDIKVLLEDHYNKNNNCSLSLIMLSGLENNKKSSKETSFNYWSNHKVNVLKDPLSKESFKFRLNFFNKKYPLDKLRNFKLNSKKLSESGFIYSPFFENDDRVSCYYCNCSLEGWEEYDDPIEEHKKNHNLYCYFLDVYSQKYDGKANAESEIKKLNNDDNIKEDIKRNGKTDHFGRLSEGKEESEGEEDENEPERIEEQKEVKEKETNNMIKDSPIISSNKKPVASVQSESVSAKKTDKDNSEIQNKSLLDATNKNSVKNNSSRSIVYIDDDVALNSNDDEVSFDDLSDFDDKIYSQEQEESSPEPSQPIRRSKRLTKIRAQKDPNDDYWNKLPDRDLYNELMGKKSKSKDNISDSKSESEFEFYDDNLSEEEILETDDASFEPDPPTPPRAINSRLRPAKKQTKNAKEDTDSPDELSPLPSNGSSYSDLITKIEPGASAGISSKLNRKQNKKKIKETIRQAVTDAATESEPVASSYSSPTKQNPAQKESSSIVLSKTPSLSPLQNINDTNDIQKKRLSANELSTGIELDLKSPRKFKKIKLVNKGYSPSPPVYDISDQNLGDYDESNLKFLENNIKPVNIDSLFLKTAKGPALKNDKDIEIDKYQPKSSPIKLNPSNTLTSGLKSKAKSKIIKPLPHSKQSKQKYNILDMSFDDEAFVVSSNKFISIQKRNNEIVPVSTRKKSISEKIPKSTILDNERDQSKVSPPKEQGSSKLDNACIKEDMDKHYSKKNNHPNKRNNFNNDNGHFSIDDSLPSDVSGSTDIESVISQFKKETSLKNYVQIESIKDVASDINKDGSDEEQREVEGAKNDDVRYGAVDELNSISNKEEVEDKFQHNVSRLQRDNDDVESSGVDKGKLVHVSNVQTNPESPTLSDNATQNKGESTITPAISDEKVNQINKNDDDTFDISNSPSIYADYIQDIKEINNEILKSLEIFTNDEDSEGMDEHDNDLKKGEQDSNSNGLSNQDHIGDALDEELLDLANPNANINTSVQLSKDSTNRSIKQIEVDKAVNINYGAEIKSDREKSIQKEEIYKGENHQEQHIIQSSEDNIKETYYLKKDADLTNGGRSSHEDNKELSNYQTSNFKIQDSLPQNGDFINLDKSRHVDSSDNRSSEISLAEGEKLNGEENEIEGSTIDQPNKDINSADNKRSSRILNTETKLNQDISDPDAKDEARKEDDITNVEKSLVKEDSAVAVIRVDEDSNPEIKATDMIYKSKFNGNEVLDQEEKSLAKRDPSNDNVISITDNAIIKSFSKEDKQEIITTRKSKSPNAGITDLSGNFNALLEASTPERREDNPVISRAGKVPSENIDWVPISLSSLAETLHNFEDTANYLKTVATSENDLHNDYDSELTNFISAMPENEEYMTIQEWVKYSASNCGKLVKETCNDIIRVYELDYFRALNVLESLPTED"  # Huidige sequentie
    header = "ORF 1"
    BLASTp(sequentie, header)


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
    orf_id = database_orf.database_orf_checker('AT')
    for i in read:
        for hit in i:
            blast_id = database_orf.database_blastid_checker()
            list_results = ['AT', hit.description, hit[0].evalue, orf_id,
                            blast_id + 1]
            scientific_name = re.search("\[([A-Z][a-z]+\s[a-z]+)", hit.description)
            if scientific_name:
                organism_name = scientific_name.group().replace("[", "").replace("]", "")
                organism_id = database_orf.database_organism_checker(organism_name)
                list_results.append(organism_id)
            else:
                organism_name = re.search("(?<=\[).+?(?=\])", hit.description).group()
                organism_id = database_orf.database_organism_checker(organism_name)
                list_results.append(organism_id)
            list_result_all.append(list_results)

    print(list_result_all)


main()
