class Commune {

  int numero;
  String nom;
  String code_postal;

  void initCommune(int dpt, String n, String c) {
    this.numero = dpt;
    this.nom = n;
    this.code_postal = c;
  }
  void affiche() {
    if ( this.numero <10) {
      println("département :0"+numero, "nom :"+nom, "code postal: 0"+code_postal);
    } else {
      println("département :"+numero, "nom :"+nom, "code postal:"+code_postal);
    }
  }
}
