package myRESTwsBean;

public class BeanJSon {
	private int codi;
	private String nom;
	
	public BeanJSon() {
		codi=0;
		nom =new String();
	}
	public int getCodi() {
		return codi;
	}
	public void setCodi(int codi) {
		this.codi=codi;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom=nom;
	}
	
}