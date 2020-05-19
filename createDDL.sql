CREATE TABLE ACCES (IDACCES INTEGER AUTO_INCREMENT NOT NULL, TITREACCES VARCHAR(255), PRIMARY KEY (IDACCES))
CREATE TABLE accounts (TKEY INTEGER AUTO_INCREMENT NOT NULL, cloturer TINYINT(1) default 0, devise VARCHAR(255), ferme TINYINT(1) default 0, HLEVEL INTEGER, ISACTIVE TINYINT(1) default 0, ISHEADER TINYINT(1) default 0, LABEL VARCHAR(255), num_cpt VARCHAR(255), solde_init DOUBLE, solde_progressif DOUBLE, parent INTEGER, PRIMARY KEY (TKEY))
CREATE TABLE ADRESSE (IDADRESSE INTEGER AUTO_INCREMENT NOT NULL, ADRESSECOMMUNE VARCHAR(255), ADRESSEDISTRICT VARCHAR(255), ADRESSEPHYSIQUE VARCHAR(255), ADRESSEREGION VARCHAR(255), ADRESSEVILLE VARCHAR(255), CODEREGION INTEGER, DISTANCEAGENCE INTEGER, PRIMARY KEY (IDADRESSE))
CREATE TABLE AGENCE (CODEAGENCE VARCHAR(255) NOT NULL, ADRESSEAGENCE VARCHAR(255), NOMAGENCE VARCHAR(255), PRIMARY KEY (CODEAGENCE))
CREATE TABLE calapresdebl (ROWID INTEGER AUTO_INCREMENT NOT NULL, DATE VARCHAR(255), MCOMM FLOAT, MINT FLOAT, MPEN FLOAT, MPRINC DOUBLE, PAYER TINYINT(1) default 0, num_credit VARCHAR(255), PRIMARY KEY (ROWID))
CREATE TABLE calpaiementdues (ROWID INTEGER AUTO_INCREMENT NOT NULL, DATE VARCHAR(255), MONTANTCOMM FLOAT, MONTANTINT FLOAT, MONTANTPENAL FLOAT, MONTANTPRINC DOUBLE, num_credit VARCHAR(255), PRIMARY KEY (ROWID))
CREATE TABLE cat_epargne (nom_cat_epargne VARCHAR(255) NOT NULL, descr_cat_epargne VARCHAR(255), PRIMARY KEY (nom_cat_epargne))
CREATE TABLE commission_credit (TCODE VARCHAR(255) NOT NULL, CASH TINYINT(1) default 0, CHEQID INTEGER, cpt_caisse_num INTEGER, date_paie VARCHAR(255), LCOMM DOUBLE, PIECE VARCHAR(255), STATIONERY DOUBLE, STATUT_COMM VARCHAR(255), TDF FLOAT, TOTVAT FLOAT, num_credit VARCHAR(255), user_id INTEGER, PRIMARY KEY (TCODE))
CREATE TABLE compte_epargne (num_compte_ep VARCHAR(255) NOT NULL, COMPTGELER TINYINT(1) default 0, date_echeance VARCHAR(255), date_ouverture VARCHAR(255), FERMER TINYINT(1) default 0, ISACTIF TINYINT(1) default 0, PASRETRAIT TINYINT(1) default 0, SOLDE DOUBLE, codeGrp VARCHAR(255), codeInd VARCHAR(255), Produit_epargneId VARCHAR(255), UtilisateuridUtilisateur INTEGER, PRIMARY KEY (num_compte_ep))
CREATE TABLE compte_ferme (ID INTEGER AUTO_INCREMENT NOT NULL, date_cloture VARCHAR(255), frais_cloture DOUBLE, NUMRECUE VARCHAR(255), RAISON VARCHAR(255), num_compte VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE comptes (ID BIGINT AUTO_INCREMENT NOT NULL, ACTIVE TINYINT(1) default 0, COMPTE VARCHAR(255), DEVISE VARCHAR(255), FERME TINYINT(1) default 0, LEVEL INTEGER, LIBELLE VARCHAR(255), solde_init DOUBLE, solde_progressif DOUBLE, TYPE VARCHAR(255), parent_id BIGINT, PRIMARY KEY (ID))
CREATE TABLE config_credit (ROWID INTEGER AUTO_INCREMENT NOT NULL, AUTORSURPAIECREDIT TINYINT(1) default 0, AUTORISECREDITPASARRIERE TINYINT(1) default 0, AVISEUSERPENALNONCALCULE TINYINT(1) default 0, CALCREMBMODIFBYAGENT TINYINT(1) default 0, CONSCREDITECHEANTENTANTARRIERE TINYINT(1) default 0, CYCLEMODIFIABLEAUDECAISS TINYINT(1) default 0, DECAISSEMENTDEFAULT VARCHAR(255), EXCLUREREMBANTICIPATIF TINYINT(1) default 0, GARANTAVALPLUSCREDIT TINYINT(1) default 0, IMPRIMCONTRATAUDECAIS TINYINT(1) default 0, IMPRIMRECUAUDECAIS TINYINT(1) default 0, MAXNBRJRTRANCHEAPRESDECAIS INTEGER, NUMCREDITMODIFIABLE TINYINT(1) default 0, PAIETRANCHEENAVANCE TINYINT(1) default 0, PEUTRECEVOIRCREDITPARALLELE TINYINT(1) default 0, REMBDEFAULT VARCHAR(255), REPORTDATEECHEANCE TINYINT(1) default 0, SOLLICITECREDITPARALLELE TINYINT(1) default 0, SUIVICREDITGRPMBR TINYINT(1) default 0, TAUXINTDIFFMBRGRP TINYINT(1) default 0, TOTALCAPINTPENCOMM TINYINT(1) default 0, PRIMARY KEY (ROWID))
CREATE TABLE config_credit_individuel (ROWID INTEGER AUTO_INCREMENT NOT NULL, ADHESIONMINAVANTDECAIS INTEGER, CALCULINTDIFF TINYINT(1) default 0, CALCULINTENJOURS TINYINT(1) default 0, `commission cr�dit` FLOAT, DELAISGRACEMAX INTEGER, DIFFEREPAIEMENT INTEGER, FORFAITPAIEPREALABLEINT TINYINT(1) default 0, INTDEDUITAUDECAISSEMENT TINYINT(1) default 0, INTPENDANTDIFFERE TINYINT(1) default 0, INTSURDIFFCAPITALISE TINYINT(1) default 0, MODECALCULINTERET VARCHAR(255), montant_credit DOUBLE, MONTANTMAXCREDIT DOUBLE, MONTANTMINCREDIT DOUBLE, PARIEMENTPREALABLEINT TINYINT(1) default 0, PERIODEMAXCREDIT INTEGER, PERIODEMINCREDIT INTEGER, TAUXINTERETANNUEL FLOAT, TRANCHEDISTINTPERIODEDIFF TINYINT(1) default 0, TRANCHES INTEGER, TYPETRANCHE VARCHAR(255), VALIDATIONMONTANTCREDITPARCYCLE TINYINT(1) default 0, PRIMARY KEY (ROWID))
CREATE TABLE config_frais_credit (ROWID INTEGER AUTO_INCREMENT NOT NULL, avantOuApres VARCHAR(255), COMMISSION DOUBLE, FRAISDEMANDEOUDECAIS TINYINT(1) default 0, FRAISDEVELOPPEMENT DOUBLE, FRAISREFINANCEMENT INTEGER, PAPETERIE DOUBLE, TAUXCOMMISSION INTEGER, TAUXFRAISDEV INTEGER, TAUXPAPETERIE INTEGER, TAUXREF INTEGER, PRIMARY KEY (ROWID))
CREATE TABLE config_garantie_credit (ROWID INTEGER AUTO_INCREMENT NOT NULL, GARANTINDOBLIG TINYINT(1) default 0, GARANTIEBASEMONTANTCREDIT TINYINT(1) default 0, GARANTIEINDOBLIG TINYINT(1) default 0, LIEREPARGNE TINYINT(1) default 0, PERCENTMONTANTGRP INTEGER, PERCENTMONTANTIND INTEGER, POURCENTAGEGARANT INTEGER, POURCENTAGEGARANTIEGRP INTEGER, POURCENTAGEGARANTIEIND INTEGER, produitEpargneId VARCHAR(255), PRIMARY KEY (ROWID))
CREATE TABLE config_general_credit (ROWID INTEGER AUTO_INCREMENT NOT NULL, ACTIVEPRIORITEREMBCREDITDECLASSSE TINYINT(1) default 0, ACTIVEREGLEDUPLUM TINYINT(1) default 0, AUTORISEDECAISPARTIEL TINYINT(1) default 0, DEVISE VARCHAR(255), ENREGPBLIPOSTDEMNDCREDIT TINYINT(1) default 0, ENREGPUBLIPOSTDECAISS TINYINT(1) default 0, EXCLUREPRDTLIMITATION TINYINT(1) default 0, NBRJRINT INTEGER, NBRSEMAINE INTEGER, NEPASREINITIALISERINTIMPAYESOLDEDEGR TINYINT(1) default 0, PEUTCHANGERCPTGLSURPAIE TINYINT(1) default 0, PRIVILEGEREMBCAPITALCREANCEDOUTEUSE TINYINT(1) default 0, PRODUITLIEEPARGNE TINYINT(1) default 0, RECALCDATEECHEANCEAUDECAIS VARCHAR(255), RECALCINTREMBAMORTDEGR TINYINT(1) default 0, TAUXINTVARSOLDEDEGR TINYINT(1) default 0, PRIMARY KEY (ROWID))
CREATE TABLE config_gl_credit (ROWID INTEGER AUTO_INCREMENT NOT NULL, COMMDEC VARCHAR(255), COMMDEM VARCHAR(255), CPTCHEQUE VARCHAR(255), CPTCOMACCGAGNEGRP VARCHAR(255), CPTCOMACCGAGNEIND VARCHAR(255), CPTCOMECHUACCGRP VARCHAR(255), CPTCOMECHUACCIND VARCHAR(255), CPTCOMMCREDIT VARCHAR(255), CPTCREDPASSEENPERTEGRP VARCHAR(255), CPTCREDPASSEENPERTEIND VARCHAR(255), CPTINTECHUARECGRP VARCHAR(255), CPTINTECHUARECIND VARCHAR(255), CPTINTECHUGRP VARCHAR(255), CPTINTECHUIND VARCHAR(255), CPTINTRECCRDTGRP VARCHAR(255), CPTINTRECCRDTIND VARCHAR(255), CPTPAPETERIE VARCHAR(255), CPTPENALCPTBLSAVAGRP VARCHAR(255), CPTPENALCPTBLSAVAIND VARCHAR(255), CPTPENALCREDIT VARCHAR(255), CPTPRINCENCOURSGRP VARCHAR(255), CPTPRINCENCOURSIND VARCHAR(255), CPTPROVCOUTMAUVCREANCEGRP VARCHAR(255), CPTPROVCOUTMAUVCREANCEIND VARCHAR(255), CPTPROVMAUVCREANCEGRP VARCHAR(255), CPTPROVMAUVCREANCEIND VARCHAR(255), CPTRECCREANDOUTEUSE VARCHAR(255), CPTREVPENALGRP VARCHAR(255), CPTREVPENALIND VARCHAR(255), CPTSURPAIEMENT VARCHAR(255), DIFFMONNAIE VARCHAR(255), FRAISDEVDEC VARCHAR(255), FRAISDEVDEM VARCHAR(255), MAJORATIONDEC VARCHAR(255), PAPETERIEDEC VARCHAR(255), PAPETERIEDEM VARCHAR(255), PROCFEEDEC VARCHAR(255), REFINFEEDEM VARCHAR(255), PRIMARY KEY (ROWID))
CREATE TABLE config_gl_epargne (ROWID INTEGER AUTO_INCREMENT NOT NULL, CHARGEINTNEGGRP VARCHAR(255), CHARGEINTNEGIND VARCHAR(255), CHARGESMSCPT VARCHAR(255), CLOTURECPTEP VARCHAR(255), COMMCHEQUEREJETE VARCHAR(255), COMMEPARGNE VARCHAR(255), COMMSURDECOUVERTS VARCHAR(255), COUTINTAPAYERGRP VARCHAR(255), COUTINTAPAYERIND VARCHAR(255), CPTATTENTE VARCHAR(255), CPTCHARGERELCPT VARCHAR(255), CPTCHEQUE VARCHAR(255), CPTPAPETERIEEP VARCHAR(255), CPTVIRECHEQUE VARCHAR(255), EPARGNEGRP VARCHAR(255), EPARGNEIND VARCHAR(255), INTCOMPTAVGRP VARCHAR(255), INTCOMPTAVIND VARCHAR(255), INTDECAPRESEXPGRP VARCHAR(255), INTDECAPRESEXPIND VARCHAR(255), INTPAYEGRP VARCHAR(255), INTPAYEIND VARCHAR(255), PENALEPARGNE VARCHAR(255), RETENUTAXE VARCHAR(255), VIREPERMCPTTIT VARCHAR(255), VIREPERMFRAISBAN VARCHAR(255), VIREPERMPENALCPT VARCHAR(255), PRIMARY KEY (ROWID))
CREATE TABLE config_interet_prod_ep (ROWID INTEGER AUTO_INCREMENT NOT NULL, DATECALCUL VARCHAR(255), INTERETMINGRP DOUBLE, INTERETMININD DOUBLE, MODECALCUL VARCHAR(255), NBRJRINT INTEGER, NBRSEMAINEINT INTEGER, PERIODEINTERET INTEGER, SOLDEMINGRP DOUBLE, SOLDEMININD DOUBLE, TAUXINTERET DOUBLE, PRIMARY KEY (ROWID))
CREATE TABLE config_penalite_credit (ROWID INTEGER AUTO_INCREMENT NOT NULL, DIFFEREPAIEMENT INTEGER, LIMITEEXPIRATIONPENAL INTEGER, MODECALCUL VARCHAR(255), montantFixe DOUBLE, PENALJRFERIE TINYINT(1) default 0, pourcentage INTEGER, PRIMARY KEY (ROWID))
CREATE TABLE config_prod_ep (ROWID INTEGER AUTO_INCREMENT NOT NULL, AGEMINCPT INTEGER, COMMRETRAITCASH DOUBLE, COMMTRANSFER DOUBLE, DEVISE VARCHAR(255), FRAISFERMETURE DOUBLE, FRAISTENUCPT DOUBLE, NBRJRIN INTEGER, NBRJRMAXDEP INTEGER, NBRJRMINRET INTEGER, PENALITEPRELEVEMNT DOUBLE, SOLDENEGATIF TINYINT(1) default 0, SOLDEOVERTURE DOUBLE, PRIMARY KEY (ROWID))
CREATE TABLE DECAISSEMENT (TCODE VARCHAR(255) NOT NULL, CASH TINYINT(1) default 0, COMMISSION DOUBLE, cpt_caisse_num VARCHAR(255), date_dec VARCHAR(255), montant_dec DOUBLE, PIECE VARCHAR(255), POCFEE FLOAT, STATIONNARY DOUBLE, TDF FLOAT, TOTVAT FLOAT, num_credit VARCHAR(255), user_id INTEGER, PRIMARY KEY (TCODE))
CREATE TABLE demande_credit (num_credit VARCHAR(255) NOT NULL, APPBY VARCHAR(255), approbation_statut VARCHAR(255), but_credit VARCHAR(255), commission TINYINT(1) default 0, date_approbation VARCHAR(255), date_demande VARCHAR(255), diffPaie INTEGER, interet DOUBLE, INTERET_TOTAL DOUBLE, modeCalculInteret VARCHAR(255), montant_approved DOUBLE, montant_demande DOUBLE, NBCREDIT INTEGER, nbTranche INTEGER, PRINCIPALE_TOTAL DOUBLE, SOLDE_TOTAL DOUBLE, taux DOUBLE, typeTranche VARCHAR(255), agent INTEGER, codeGrp VARCHAR(255), codeInd VARCHAR(255), produitCredit_id VARCHAR(255), user_id INTEGER, PRIMARY KEY (num_credit))
CREATE TABLE DOCIDENTITE (NUMERO VARCHAR(255) NOT NULL, DATEEMIS DATE, DATEEXPIRE DATE, DELIVREPAR VARCHAR(255), PRIORITE INTEGER, TYPEDOC VARCHAR(255), codeGarant VARCHAR(15), codeClient VARCHAR(255), PRIMARY KEY (NUMERO))
CREATE TABLE ENTREPRISE (CODECLIE VARCHAR(255) AUTO_INCREMENT NOT NULL, CONTACTE VARCHAR(255), DATEINSCRITE DATE, NOME VARCHAR(255), idAdresse INTEGER, PRIMARY KEY (CODECLIE))
CREATE TABLE fichier_docidentite (CODEDOCUMENT VARCHAR(255) AUTO_INCREMENT NOT NULL, DELIVREPAR VARCHAR(255), FORMAT VARCHAR(255), ISOBLIGATOIRE TINYINT, ISUNIQUE TINYINT, LARGEUR INTEGER, NOM VARCHAR(255), PRIMARY KEY (CODEDOCUMENT))
CREATE TABLE fichier_langue (LANGUE VARCHAR(255) AUTO_INCREMENT NOT NULL, PAYS VARCHAR(255), PRIMARY KEY (LANGUE))
CREATE TABLE fichier_niveauetude (NIVEAUETUDE VARCHAR(255) AUTO_INCREMENT NOT NULL, DESCRIPTION VARCHAR(255), PRIMARY KEY (NIVEAUETUDE))
CREATE TABLE fichier_regiongeo (CODEREGION INTEGER AUTO_INCREMENT NOT NULL, REGION VARCHAR(255), PRIMARY KEY (CODEREGION))
CREATE TABLE fichier_titre (TITRE VARCHAR(255) AUTO_INCREMENT NOT NULL, DESCRIPTION VARCHAR(255), PRIMARY KEY (TITRE))
CREATE TABLE FONCTION (IDFONCTION INTEGER AUTO_INCREMENT NOT NULL, LIBELLEFONCTION VARCHAR(255), PRIMARY KEY (IDFONCTION))
CREATE TABLE garantie_credit (ROWID INTEGER AUTO_INCREMENT NOT NULL, LEVER TINYINT(1) default 0, nomGarantie VARCHAR(255), POURCENTAGE DOUBLE, RAISONLEVER VARCHAR(255), type_garantie VARCHAR(255), valeur DOUBLE, num_credit VARCHAR(255), PRIMARY KEY (ROWID))
CREATE TABLE GRANDLIVRE (ID VARCHAR(255) AUTO_INCREMENT NOT NULL, CLOTURE TINYINT(1) default 0, COMPTE VARCHAR(255), CREDIT DOUBLE, DATE VARCHAR(255), DEBIT DOUBLE, DESCR VARCHAR(255), PIECE VARCHAR(255), solde DOUBLE, TCODE VARCHAR(255), TIERS VARCHAR(255), user_id VARCHAR(255), num_compte_compta INTEGER, code_agence VARCHAR(255), code_analytique VARCHAR(10), code_budget VARCHAR(10), code_client VARCHAR(255), compte_dat VARCHAR(255), compte_epargne VARCHAR(255), num_credit VARCHAR(255), code_grp VARCHAR(255), utilisateur INTEGER, PRIMARY KEY (ID))
CREATE TABLE GROUPE (CODEGRP VARCHAR(255) NOT NULL, NUMSTAT VARCHAR(255), DATEINSCRIPTION VARCHAR(255), EMAIL VARCHAR(255), NOMGROUPE VARCHAR(255), NUMREFERENCE VARCHAR(255), NUMEROMOBILE VARCHAR(255), PRESIDENT VARCHAR(255), SECRETAIRE VARCHAR(255), TRESORIER VARCHAR(255), idAdresse INTEGER, PRIMARY KEY (CODEGRP))
CREATE TABLE INDIVIDUEL (CODEIND VARCHAR(255) NOT NULL, approuver TINYINT(1) default 0, code_Garant VARCHAR(15), DATEINSCRIPTION VARCHAR(255), DATENAISSANCE VARCHAR(255), DATESORTIE DATE, EMAIL VARCHAR(255), ESTCLIENTINDIVIDUEL TINYINT(1) default 0, ESTGARANT TINYINT(1) default 0, ESTMEMBREGROUPE TINYINT(1) default 0, ETATCIVIL VARCHAR(255), is_liste_noir TINYINT(1) default 0, is_liste_rouge TINYINT(1) default 0, is_sain TINYINT(1) default 0, LANGUE VARCHAR(255), LIEUNAISSANCE VARCHAR(255), NBENFANT INTEGER, NBPERSCHARGE INTEGER, NIVEAUETUDE VARCHAR(255), NOMCLIENT VARCHAR(255), NOMCONJOINT VARCHAR(255), NUMEROMOBILE VARCHAR(255), PARENTADRESSE VARCHAR(255), PARENTNOM VARCHAR(255), PRENOMCLIENT VARCHAR(255), PROFESSION VARCHAR(255), RAISONSORTIE VARCHAR(255), SEXE VARCHAR(255), TITRE VARCHAR(255), idAdresse INTEGER, codeGrp VARCHAR(255), PRIMARY KEY (CODEIND))
CREATE TABLE produit_credit (id_prod_credit VARCHAR(255) NOT NULL, ETAT TINYINT(1) default 0, nom_prod_credit VARCHAR(255), configFraisGroupe_id INTEGER, config_id INTEGER, configCreditGroup_id INTEGER, configCreditInd_id INTEGER, configGL2Id INTEGER, configFrais_id INTEGER, configGarantie_id INTEGER, configGeneral_id INTEGER, configGLcredId INTEGER, configPenaliteId INTEGER, PRIMARY KEY (id_prod_credit))
CREATE TABLE produit_epargne (id_prod_epargne VARCHAR(255) NOT NULL, ETAT TINYINT(1) default 0, nom_prod_epargne VARCHAR(255), configGeneralDat INTEGER, configGlDat INTEGER, configGLepId INTEGER, configIntProId INTEGER, configProdId INTEGER, Type_epargnenom_type_epargne VARCHAR(255), PRIMARY KEY (id_prod_epargne))
CREATE TABLE remb_montant (TCODE INTEGER AUTO_INCREMENT NOT NULL, date_remb DATETIME, MCOMM FLOAT, MINT DOUBLE, MPEN FLOAT, MPRINC DOUBLE, TOTVAT FLOAT, num_credit VARCHAR(255), PRIMARY KEY (TCODE))
CREATE TABLE remboursement (TCODE VARCHAR(255) NOT NULL, CHEQCOMM FLOAT, cpt_caisse_num VARCHAR(255), date_remb VARCHAR(255), INTERET DOUBLE, MONTANT_PAYE DOUBLE, nbEcheance INTEGER, OVERPAY FLOAT, PIECE VARCHAR(255), PRINCIPAL DOUBLE, RESTAPAIE DOUBLE, solde DOUBLE, STATIONERY FLOAT, totalInteret DOUBLE, totalPrincipale DOUBLE, typeAction VARCHAR(255), typePaie VARCHAR(255), valPaie VARCHAR(255), num_credit VARCHAR(255), user_id INTEGER, PRIMARY KEY (TCODE))
CREATE TABLE transaction_epargne (tcode_ep VARCHAR(255) NOT NULL, comm_retrait DOUBLE, comm_trans DOUBLE, date_transaction VARCHAR(255), DESCRIPTION VARCHAR(255), MONTANT DOUBLE, commPrelev DOUBLE, piece_compta VARCHAR(255), SOLDE DOUBLE, type_paiement VARCHAR(50) NOT NULL, type_trans_ep VARCHAR(255), val_paie VARCHAR(50), id_caisse VARCHAR(255), Compte_epargnenum_compte_ep VARCHAR(255), PRIMARY KEY (tcode_ep))
CREATE TABLE type_epargne (nom_type_epargne VARCHAR(255) NOT NULL, abrev VARCHAR(255), descr_type_epargne VARCHAR(255), Cat_epargnenom_cat_epargne VARCHAR(255), PRIMARY KEY (nom_type_epargne))
CREATE TABLE UTILISATEUR (IDUTILISATEUR INTEGER AUTO_INCREMENT NOT NULL, GENREUSER VARCHAR(255), LOGINUTILISATEUR VARCHAR(255), MDPUTILISATEUR VARCHAR(255), NOMUTILISATEUR VARCHAR(255), photo VARCHAR(255), TELUSER VARCHAR(255), fonctionId INTEGER, PRIMARY KEY (IDUTILISATEUR))
CREATE TABLE VIREMENT (id_virement INTEGER AUTO_INCREMENT NOT NULL, date_virement VARCHAR(255), MONTANT DOUBLE, piece_compta VARCHAR(255), numCptAdebite VARCHAR(255), numCptAcredite VARCHAR(255), PRIMARY KEY (id_virement))
CREATE TABLE config_credit_group (ROWID INTEGER AUTO_INCREMENT NOT NULL, ADHESIONMINGROUP INTEGER, ADHESIONMINMEMBRE INTEGER, CALCULINTDIFF TINYINT(1) default 0, CALCULINTJOURS TINYINT(1) default 0, CALCULEINTERET VARCHAR(255), COMMISSIONCREDIT DOUBLE, DELAIMAX INTEGER, DIFFPAIEMENT INTEGER, FORFAITPAIEINT TINYINT(1) default 0, GARANTIEVERIFIEMEMBRE TINYINT(1) default 0, INTDEDUITAUDECAIS TINYINT(1) default 0, INTDIFFPAIECAPITAL TINYINT(1) default 0, INTPAIEDIFF TINYINT(1) default 0, INTERETANNUEL DOUBLE, MONTANMINPARMEMBRE DOUBLE, MONTANTCREDIT DOUBLE, MONTANTCREDITMEMBRE DOUBLE, MONTANTMAXPARMEMBRE DOUBLE, NUMCYCLEMEMBRE INTEGER, PAIEPREALABLEINT TINYINT(1) default 0, PERIODEMAXCREDIT INTEGER, PERIODEMINCREDIT INTEGER, SECTEURACTIV VARCHAR(255), TRANCHE INTEGER, TRANCHEDISTINTPERIODE TINYINT(1) default 0, TYPETRANCHE VARCHAR(255), PRIMARY KEY (ROWID))
CREATE TABLE droitinscription (ROWID INTEGER AUTO_INCREMENT NOT NULL, COMPTECAISSE VARCHAR(255), DATE_PAYE VARCHAR(255), DROITS DOUBLE, FRAISADMIN DOUBLE, FRAISDOSSIER DOUBLE, PIECE VARCHAR(255), codeInd VARCHAR(255), codeGrp VARCHAR(255), PRIMARY KEY (ROWID))
CREATE TABLE liste_rouge (ID INTEGER AUTO_INCREMENT NOT NULL, DATE VARCHAR(255), RAISON VARCHAR(255), ROUGE TINYINT(1) default 0, codeGroupe VARCHAR(255), codeInd VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE config_frais_groupe (ROWID INTEGER AUTO_INCREMENT NOT NULL, avantOuApres VARCHAR(255), COMMISSION DOUBLE, FRAISDEMANDEOUDECAIS TINYINT(1) default 0, FRAISDEVELOPPEMENT DOUBLE, FRAISREFINANCEMENT INTEGER, PAPETERIE DOUBLE, TAUXCOMMISSION INTEGER, TAUXFRAISDEV INTEGER, TAUXPAPETERIE INTEGER, TAUXREF INTEGER, PRIMARY KEY (ROWID))
CREATE TABLE interet_epargne (ID INTEGER AUTO_INCREMENT NOT NULL, DATE VARCHAR(255), MONTANT DOUBLE, solde DOUBLE, num_compte VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE analytique (id VARCHAR(10) NOT NULL, nom VARCHAR(50), PRIMARY KEY (id))
CREATE TABLE budget (code VARCHAR(10) NOT NULL, date VARCHAR(10), description VARCHAR(250), nom VARCHAR(50), prevision DOUBLE, realisation DOUBLE, PRIMARY KEY (code))
CREATE TABLE personnel_institution (CODEPERSONNEL VARCHAR(255) NOT NULL, NOM VARCHAR(255), PRENOM VARCHAR(255), id_agence VARCHAR(255), id_fonction INTEGER, PRIMARY KEY (CODEPERSONNEL))
CREATE TABLE garant_credit (codeGarant VARCHAR(15) NOT NULL, code_individuel VARCHAR(15), date_inscription VARCHAR(15), dateNais VARCHAR(15), email VARCHAR(255), estIndividuel TINYINT(1) default 0, total_montant DOUBLE, nom VARCHAR(100), prenom VARCHAR(100), profession VARCHAR(25), sexe VARCHAR(10), taux_cred DOUBLE, idAdresse INTEGER, numCredit VARCHAR(255), PRIMARY KEY (codeGarant))
CREATE TABLE calendrierView (ROWID INTEGER AUTO_INCREMENT NOT NULL, DATE VARCHAR(255), MONTANTCOMM FLOAT, MONTANTINT FLOAT, MONTANTPENAL FLOAT, MONTANTPRINC DOUBLE, NUMCRED VARCHAR(255), PRIMARY KEY (ROWID))
CREATE TABLE config_general_dat (ID INTEGER AUTO_INCREMENT NOT NULL, AUCCUNINTERET TINYINT(1) default 0, CALCULINTENJRS TINYINT(1) default 0, DEPOTMAX DOUBLE, DEPOTMIN DOUBLE, DEVISE VARCHAR(255), FINPREMATURE INTEGER, INTERMAX INTEGER, INTERMIN INTEGER, MONTANTPREMATURE DOUBLE, NBJRANNEE INTEGER, PERIODEMAXINT INTEGER, PERIODEMININT INTEGER, TAXE INTEGER, PRIMARY KEY (ID))
CREATE TABLE config_GL_DAT (ID INTEGER AUTO_INCREMENT NOT NULL, CMPTACCUMULE VARCHAR(255), CMPTCHEQUE VARCHAR(255), CMPTDAT VARCHAR(255), CMPTDIFFCASH VARCHAR(255), CMPTINTDUDAT VARCHAR(255), CMPTINTECHUS VARCHAR(255), CMPTINTPAYEDAT VARCHAR(255), CMPTPENALDAT VARCHAR(255), CMPTTAXERETENU VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE compte_dat (num_compte_dat VARCHAR(255) NOT NULL, DATEDEPOT VARCHAR(255), DATEECHEANCE VARCHAR(255), DATERETRAIT VARCHAR(255), FERMER TINYINT(1) default 0, INTERET DOUBLE, MODEPAYEINTERET VARCHAR(255), MONTANT DOUBLE, PAYEINT VARCHAR(255), penalite DOUBLE, PERIODE INTEGER, RAISON VARCHAR(255), RET TINYINT(1) default 0, TAUXINT INTEGER, taxe DOUBLE, total DOUBLE, TOTALINTRETRAIT DOUBLE, codeGrp VARCHAR(255), codeInd VARCHAR(255), Produit_epargneId VARCHAR(255), UtilisateuridUtilisateur INTEGER, PRIMARY KEY (num_compte_dat))
CREATE TABLE caisse (nom_caisse VARCHAR(255) NOT NULL, devise VARCHAR(255), id_compte INTEGER, PRIMARY KEY (nom_caisse))
CREATE TABLE Garantie_view (ROWID INTEGER AUTO_INCREMENT NOT NULL, nomGarantie VARCHAR(255), numCredit VARCHAR(255), pourcentage DOUBLE, type_garantie VARCHAR(255), valeur DOUBLE, PRIMARY KEY (ROWID))
CREATE TABLE fiche_credit (ID INTEGER AUTO_INCREMENT NOT NULL, DATE VARCHAR(255), INTERET DOUBLE, num_credit VARCHAR(255), PENALITE DOUBLE, PIECE VARCHAR(255), PRINCIPALE DOUBLE, SOLDECOURANT DOUBLE, TOTALINTERET DOUBLE, TOTALPRINCIPAL DOUBLE, TOTALSOLDE DOUBLE, TRANSACTION VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE config_declasse (ROWID INTEGER AUTO_INCREMENT NOT NULL, COMPTGLGRP VARCHAR(255), COMPTGLIND VARCHAR(255), DEBUT INTEGER, FIN INTEGER, PRIMARY KEY (ROWID))
CREATE TABLE membre_groupe (IDMEMBRE INTEGER AUTO_INCREMENT NOT NULL, id_fonction INTEGER, code_groupe VARCHAR(255), code_individuel VARCHAR(255), PRIMARY KEY (IDMEMBRE))
CREATE TABLE approbationCredit (ID INTEGER AUTO_INCREMENT NOT NULL, DATEAP VARCHAR(255), DESCRIPTION VARCHAR(255), MONTANTAPPROUVER DOUBLE, PERSONEAP VARCHAR(255), num_credit VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE montant_credit_membre (ID INTEGER AUTO_INCREMENT NOT NULL, INTERET DOUBLE, PRINCIPALE DOUBLE, TAUXINT INTEGER, numCredit VARCHAR(255), codeGrp VARCHAR(255), codeInd VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE credit_groupe_view (ID INTEGER AUTO_INCREMENT NOT NULL, CODEGRP VARCHAR(255), CODEIND VARCHAR(255), INTERET DOUBLE, NUMCREDIT VARCHAR(255), PRINCIPALE DOUBLE, TAUXINT INTEGER, PRIMARY KEY (ID))
CREATE TABLE fonction_membre (ID INTEGER AUTO_INCREMENT NOT NULL, NOMFONCTION VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE membre_view (ID INTEGER AUTO_INCREMENT NOT NULL, CODEGROUPE VARCHAR(255), CODEIND VARCHAR(255), FONCTION VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE config_transaction_compta (ROWID INTEGER AUTO_INCREMENT NOT NULL, compteBanque INTEGER, compteCaisse INTEGER, PRIMARY KEY (ROWID))
CREATE TABLE operation_view (ID INTEGER AUTO_INCREMENT NOT NULL, CREDIT DOUBLE, DEBIT DOUBLE, LABEL VARCHAR(255), NUMCMPT VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE fonction_acces (AccesidAcces INTEGER NOT NULL, FonctionidFonction INTEGER NOT NULL, PRIMARY KEY (AccesidAcces, FonctionidFonction))
CREATE TABLE utilisateur_agence (AgencecodeAgence VARCHAR(255) NOT NULL, UtilisateuridUtilisateur INTEGER NOT NULL, PRIMARY KEY (AgencecodeAgence, UtilisateuridUtilisateur))
CREATE TABLE utilisateur_compte_caisse (UtilisateuridUtilisateur INTEGER NOT NULL, Compte_caissenom_cpt_caisse VARCHAR(255) NOT NULL, PRIMARY KEY (UtilisateuridUtilisateur, Compte_caissenom_cpt_caisse))
ALTER TABLE accounts ADD CONSTRAINT FK_accounts_parent FOREIGN KEY (parent) REFERENCES accounts (TKEY)
ALTER TABLE calapresdebl ADD CONSTRAINT FK_calapresdebl_num_credit FOREIGN KEY (num_credit) REFERENCES demande_credit (num_credit)
ALTER TABLE calpaiementdues ADD CONSTRAINT FK_calpaiementdues_num_credit FOREIGN KEY (num_credit) REFERENCES demande_credit (num_credit)
ALTER TABLE commission_credit ADD CONSTRAINT FK_commission_credit_num_credit FOREIGN KEY (num_credit) REFERENCES demande_credit (num_credit)
ALTER TABLE commission_credit ADD CONSTRAINT FK_commission_credit_user_id FOREIGN KEY (user_id) REFERENCES UTILISATEUR (IDUTILISATEUR)
ALTER TABLE compte_epargne ADD CONSTRAINT FK_compte_epargne_UtilisateuridUtilisateur FOREIGN KEY (UtilisateuridUtilisateur) REFERENCES UTILISATEUR (IDUTILISATEUR)
ALTER TABLE compte_epargne ADD CONSTRAINT FK_compte_epargne_codeGrp FOREIGN KEY (codeGrp) REFERENCES GROUPE (CODEGRP)
ALTER TABLE compte_epargne ADD CONSTRAINT FK_compte_epargne_Produit_epargneId FOREIGN KEY (Produit_epargneId) REFERENCES produit_epargne (id_prod_epargne)
ALTER TABLE compte_epargne ADD CONSTRAINT FK_compte_epargne_codeInd FOREIGN KEY (codeInd) REFERENCES INDIVIDUEL (CODEIND)
ALTER TABLE compte_ferme ADD CONSTRAINT FK_compte_ferme_num_compte FOREIGN KEY (num_compte) REFERENCES compte_epargne (num_compte_ep)
ALTER TABLE comptes ADD CONSTRAINT FK_comptes_parent_id FOREIGN KEY (parent_id) REFERENCES comptes (ID)
ALTER TABLE config_garantie_credit ADD CONSTRAINT FK_config_garantie_credit_produitEpargneId FOREIGN KEY (produitEpargneId) REFERENCES produit_epargne (id_prod_epargne)
ALTER TABLE DECAISSEMENT ADD CONSTRAINT FK_DECAISSEMENT_user_id FOREIGN KEY (user_id) REFERENCES UTILISATEUR (IDUTILISATEUR)
ALTER TABLE DECAISSEMENT ADD CONSTRAINT FK_DECAISSEMENT_num_credit FOREIGN KEY (num_credit) REFERENCES demande_credit (num_credit)
ALTER TABLE demande_credit ADD CONSTRAINT FK_demande_credit_agent FOREIGN KEY (agent) REFERENCES UTILISATEUR (IDUTILISATEUR)
ALTER TABLE demande_credit ADD CONSTRAINT FK_demande_credit_codeGrp FOREIGN KEY (codeGrp) REFERENCES GROUPE (CODEGRP)
ALTER TABLE demande_credit ADD CONSTRAINT FK_demande_credit_produitCredit_id FOREIGN KEY (produitCredit_id) REFERENCES produit_credit (id_prod_credit)
ALTER TABLE demande_credit ADD CONSTRAINT FK_demande_credit_codeInd FOREIGN KEY (codeInd) REFERENCES INDIVIDUEL (CODEIND)
ALTER TABLE demande_credit ADD CONSTRAINT FK_demande_credit_user_id FOREIGN KEY (user_id) REFERENCES UTILISATEUR (IDUTILISATEUR)
ALTER TABLE DOCIDENTITE ADD CONSTRAINT FK_DOCIDENTITE_codeGarant FOREIGN KEY (codeGarant) REFERENCES garant_credit (codeGarant)
ALTER TABLE DOCIDENTITE ADD CONSTRAINT FK_DOCIDENTITE_codeClient FOREIGN KEY (codeClient) REFERENCES INDIVIDUEL (CODEIND)
ALTER TABLE ENTREPRISE ADD CONSTRAINT FK_ENTREPRISE_idAdresse FOREIGN KEY (idAdresse) REFERENCES ADRESSE (IDADRESSE)
ALTER TABLE garantie_credit ADD CONSTRAINT FK_garantie_credit_num_credit FOREIGN KEY (num_credit) REFERENCES demande_credit (num_credit)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_code_client FOREIGN KEY (code_client) REFERENCES INDIVIDUEL (CODEIND)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_code_budget FOREIGN KEY (code_budget) REFERENCES budget (code)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_code_agence FOREIGN KEY (code_agence) REFERENCES AGENCE (CODEAGENCE)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_num_credit FOREIGN KEY (num_credit) REFERENCES demande_credit (num_credit)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_num_compte_compta FOREIGN KEY (num_compte_compta) REFERENCES accounts (TKEY)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_compte_epargne FOREIGN KEY (compte_epargne) REFERENCES compte_epargne (num_compte_ep)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_code_grp FOREIGN KEY (code_grp) REFERENCES GROUPE (CODEGRP)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_utilisateur FOREIGN KEY (utilisateur) REFERENCES UTILISATEUR (IDUTILISATEUR)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_compte_dat FOREIGN KEY (compte_dat) REFERENCES compte_dat (num_compte_dat)
ALTER TABLE GRANDLIVRE ADD CONSTRAINT FK_GRANDLIVRE_code_analytique FOREIGN KEY (code_analytique) REFERENCES analytique (id)
ALTER TABLE GROUPE ADD CONSTRAINT FK_GROUPE_idAdresse FOREIGN KEY (idAdresse) REFERENCES ADRESSE (IDADRESSE)
ALTER TABLE INDIVIDUEL ADD CONSTRAINT FK_INDIVIDUEL_codeGrp FOREIGN KEY (codeGrp) REFERENCES GROUPE (CODEGRP)
ALTER TABLE INDIVIDUEL ADD CONSTRAINT FK_INDIVIDUEL_idAdresse FOREIGN KEY (idAdresse) REFERENCES ADRESSE (IDADRESSE)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_configGarantie_id FOREIGN KEY (configGarantie_id) REFERENCES config_garantie_credit (ROWID)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_configGL2Id FOREIGN KEY (configGL2Id) REFERENCES config_declasse (ROWID)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_configFrais_id FOREIGN KEY (configFrais_id) REFERENCES config_frais_credit (ROWID)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_configCreditGroup_id FOREIGN KEY (configCreditGroup_id) REFERENCES config_credit_group (ROWID)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_configGeneral_id FOREIGN KEY (configGeneral_id) REFERENCES config_general_credit (ROWID)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_configFraisGroupe_id FOREIGN KEY (configFraisGroupe_id) REFERENCES config_frais_groupe (ROWID)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_configGLcredId FOREIGN KEY (configGLcredId) REFERENCES config_gl_credit (ROWID)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_config_id FOREIGN KEY (config_id) REFERENCES config_credit (ROWID)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_configPenaliteId FOREIGN KEY (configPenaliteId) REFERENCES config_penalite_credit (ROWID)
ALTER TABLE produit_credit ADD CONSTRAINT FK_produit_credit_configCreditInd_id FOREIGN KEY (configCreditInd_id) REFERENCES config_credit_individuel (ROWID)
ALTER TABLE produit_epargne ADD CONSTRAINT FK_produit_epargne_configIntProId FOREIGN KEY (configIntProId) REFERENCES config_interet_prod_ep (ROWID)
ALTER TABLE produit_epargne ADD CONSTRAINT FK_produit_epargne_configGlDat FOREIGN KEY (configGlDat) REFERENCES config_GL_DAT (ID)
ALTER TABLE produit_epargne ADD CONSTRAINT FK_produit_epargne_Type_epargnenom_type_epargne FOREIGN KEY (Type_epargnenom_type_epargne) REFERENCES type_epargne (nom_type_epargne)
ALTER TABLE produit_epargne ADD CONSTRAINT FK_produit_epargne_configProdId FOREIGN KEY (configProdId) REFERENCES config_prod_ep (ROWID)
ALTER TABLE produit_epargne ADD CONSTRAINT FK_produit_epargne_configGLepId FOREIGN KEY (configGLepId) REFERENCES config_gl_epargne (ROWID)
ALTER TABLE produit_epargne ADD CONSTRAINT FK_produit_epargne_configGeneralDat FOREIGN KEY (configGeneralDat) REFERENCES config_general_dat (ID)
ALTER TABLE remb_montant ADD CONSTRAINT FK_remb_montant_num_credit FOREIGN KEY (num_credit) REFERENCES demande_credit (num_credit)
ALTER TABLE remboursement ADD CONSTRAINT FK_remboursement_num_credit FOREIGN KEY (num_credit) REFERENCES demande_credit (num_credit)
ALTER TABLE remboursement ADD CONSTRAINT FK_remboursement_user_id FOREIGN KEY (user_id) REFERENCES UTILISATEUR (IDUTILISATEUR)
ALTER TABLE transaction_epargne ADD CONSTRAINT FK_transaction_epargne_Compte_epargnenum_compte_ep FOREIGN KEY (Compte_epargnenum_compte_ep) REFERENCES compte_epargne (num_compte_ep)
ALTER TABLE transaction_epargne ADD CONSTRAINT FK_transaction_epargne_id_caisse FOREIGN KEY (id_caisse) REFERENCES caisse (nom_caisse)
ALTER TABLE type_epargne ADD CONSTRAINT FK_type_epargne_Cat_epargnenom_cat_epargne FOREIGN KEY (Cat_epargnenom_cat_epargne) REFERENCES cat_epargne (nom_cat_epargne)
ALTER TABLE UTILISATEUR ADD CONSTRAINT FK_UTILISATEUR_fonctionId FOREIGN KEY (fonctionId) REFERENCES FONCTION (IDFONCTION)
ALTER TABLE VIREMENT ADD CONSTRAINT FK_VIREMENT_numCptAcredite FOREIGN KEY (numCptAcredite) REFERENCES compte_epargne (num_compte_ep)
ALTER TABLE VIREMENT ADD CONSTRAINT FK_VIREMENT_numCptAdebite FOREIGN KEY (numCptAdebite) REFERENCES compte_epargne (num_compte_ep)
ALTER TABLE droitinscription ADD CONSTRAINT FK_droitinscription_codeInd FOREIGN KEY (codeInd) REFERENCES INDIVIDUEL (CODEIND)
ALTER TABLE droitinscription ADD CONSTRAINT FK_droitinscription_codeGrp FOREIGN KEY (codeGrp) REFERENCES GROUPE (CODEGRP)
ALTER TABLE liste_rouge ADD CONSTRAINT FK_liste_rouge_codeGroupe FOREIGN KEY (codeGroupe) REFERENCES GROUPE (CODEGRP)
ALTER TABLE liste_rouge ADD CONSTRAINT FK_liste_rouge_codeInd FOREIGN KEY (codeInd) REFERENCES INDIVIDUEL (CODEIND)
ALTER TABLE interet_epargne ADD CONSTRAINT FK_interet_epargne_num_compte FOREIGN KEY (num_compte) REFERENCES compte_epargne (num_compte_ep)
ALTER TABLE personnel_institution ADD CONSTRAINT FK_personnel_institution_id_fonction FOREIGN KEY (id_fonction) REFERENCES FONCTION (IDFONCTION)
ALTER TABLE personnel_institution ADD CONSTRAINT FK_personnel_institution_id_agence FOREIGN KEY (id_agence) REFERENCES AGENCE (CODEAGENCE)
ALTER TABLE garant_credit ADD CONSTRAINT FK_garant_credit_idAdresse FOREIGN KEY (idAdresse) REFERENCES ADRESSE (IDADRESSE)
ALTER TABLE garant_credit ADD CONSTRAINT FK_garant_credit_numCredit FOREIGN KEY (numCredit) REFERENCES demande_credit (num_credit)
ALTER TABLE compte_dat ADD CONSTRAINT FK_compte_dat_codeGrp FOREIGN KEY (codeGrp) REFERENCES GROUPE (CODEGRP)
ALTER TABLE compte_dat ADD CONSTRAINT FK_compte_dat_Produit_epargneId FOREIGN KEY (Produit_epargneId) REFERENCES produit_epargne (id_prod_epargne)
ALTER TABLE compte_dat ADD CONSTRAINT FK_compte_dat_codeInd FOREIGN KEY (codeInd) REFERENCES INDIVIDUEL (CODEIND)
ALTER TABLE compte_dat ADD CONSTRAINT FK_compte_dat_UtilisateuridUtilisateur FOREIGN KEY (UtilisateuridUtilisateur) REFERENCES UTILISATEUR (IDUTILISATEUR)
ALTER TABLE caisse ADD CONSTRAINT FK_caisse_id_compte FOREIGN KEY (id_compte) REFERENCES accounts (TKEY)
ALTER TABLE membre_groupe ADD CONSTRAINT FK_membre_groupe_code_individuel FOREIGN KEY (code_individuel) REFERENCES INDIVIDUEL (CODEIND)
ALTER TABLE membre_groupe ADD CONSTRAINT FK_membre_groupe_code_groupe FOREIGN KEY (code_groupe) REFERENCES GROUPE (CODEGRP)
ALTER TABLE membre_groupe ADD CONSTRAINT FK_membre_groupe_id_fonction FOREIGN KEY (id_fonction) REFERENCES fonction_membre (ID)
ALTER TABLE approbationCredit ADD CONSTRAINT FK_approbationCredit_num_credit FOREIGN KEY (num_credit) REFERENCES demande_credit (num_credit)
ALTER TABLE montant_credit_membre ADD CONSTRAINT FK_montant_credit_membre_codeGrp FOREIGN KEY (codeGrp) REFERENCES GROUPE (CODEGRP)
ALTER TABLE montant_credit_membre ADD CONSTRAINT FK_montant_credit_membre_codeInd FOREIGN KEY (codeInd) REFERENCES INDIVIDUEL (CODEIND)
ALTER TABLE montant_credit_membre ADD CONSTRAINT FK_montant_credit_membre_numCredit FOREIGN KEY (numCredit) REFERENCES demande_credit (num_credit)
ALTER TABLE config_transaction_compta ADD CONSTRAINT FK_config_transaction_compta_compteCaisse FOREIGN KEY (compteCaisse) REFERENCES accounts (TKEY)
ALTER TABLE config_transaction_compta ADD CONSTRAINT FK_config_transaction_compta_compteBanque FOREIGN KEY (compteBanque) REFERENCES accounts (TKEY)
ALTER TABLE fonction_acces ADD CONSTRAINT FK_fonction_acces_FonctionidFonction FOREIGN KEY (FonctionidFonction) REFERENCES FONCTION (IDFONCTION)
ALTER TABLE fonction_acces ADD CONSTRAINT FK_fonction_acces_AccesidAcces FOREIGN KEY (AccesidAcces) REFERENCES ACCES (IDACCES)
ALTER TABLE utilisateur_agence ADD CONSTRAINT FK_utilisateur_agence_AgencecodeAgence FOREIGN KEY (AgencecodeAgence) REFERENCES AGENCE (CODEAGENCE)
ALTER TABLE utilisateur_agence ADD CONSTRAINT FK_utilisateur_agence_UtilisateuridUtilisateur FOREIGN KEY (UtilisateuridUtilisateur) REFERENCES UTILISATEUR (IDUTILISATEUR)
ALTER TABLE utilisateur_compte_caisse ADD CONSTRAINT utilisateurcomptecaisseCompte_caissenom_cpt_caisse FOREIGN KEY (Compte_caissenom_cpt_caisse) REFERENCES caisse (nom_caisse)
ALTER TABLE utilisateur_compte_caisse ADD CONSTRAINT utilisateur_compte_caisse_UtilisateuridUtilisateur FOREIGN KEY (UtilisateuridUtilisateur) REFERENCES UTILISATEUR (IDUTILISATEUR)
