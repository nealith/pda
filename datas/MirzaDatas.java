package pda.datas;

import java.util.ArrayList;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import javax.imageio.*; 
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.Color;
import java.awt.image.Raster;
import java.util.Stack;
import java.awt.Graphics;


public class MirzaDatas {

	private String cheminOrigine;

	private String formatOrigine;

	private BufferedImage imageCourante;

	private Stack<BufferedImage> pileDeModification;

	private int hauteur;

	private int largeur;

	public MirzaDatas(){
		pileDeModification = new Stack<BufferedImage>();
	}

	public void charger(String path){
		
		pileDeModification.clear();

		cheminOrigine = path;

		//Obtention du format du fichier

		boolean finit = false;
		int i = 0;
		while(!finit){

			if (Character.compare(path.charAt(i),'.') == 0 ){
				finit = true;
			}
			else {
				i++;
			}

		}

		formatOrigine = path.substring(i+1);

		formatOrigine = formatOrigine.trim();

		//Chargement de l'image dans imageCourante

		File f = null;
		FileImageInputStream in = null;

		try {
			f = new File(path);
		
		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {
			in = new FileImageInputStream(f);
		
		} catch (Exception e) {

			System.out.println(e.getMessage());

		}	

		try {

			imageCourante = ImageIO.read(in);

	
		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		Raster grille = imageCourante.getData();

		hauteur = grille.getHeight();
		largeur = grille.getWidth();

	}

	public void sauvegarder(){

		this.sauver(cheminOrigine);

	}

	public void sauvegarder(String path){

		this.sauver(path);

	}

	private void sauver(String path){

		File f = null;
		FileImageOutputStream out = null;

		try {
			f = new File(path);
		
		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {
			out = new FileImageOutputStream(f);
		
		} catch (Exception e) {

			System.out.println(e.getMessage());

		}		


		try {

			if( !ImageIO.write(imageCourante,formatOrigine,out) ){
				System.out.println("attention, problème, bad format : "+formatOrigine);
			}

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}


	}

	public void annulerModif(){

		this.depiler();

	}

	public void setNoirEtBlanc(){

		this.empiler();

		int mRGB = 0;
		Color uneCouleur;

		for (int i = 0; i < largeur ; i++ ) {

			for (int j = 0; j < hauteur ; j++ ) {
				
				uneCouleur = new Color(imageCourante.getRGB(i,j));

				mRGB = (int) (uneCouleur.getBlue() + uneCouleur.getGreen() + uneCouleur.getRed()) / 3;

				uneCouleur = new Color(mRGB,mRGB,mRGB);

				imageCourante.setRGB(i,j,uneCouleur.getRGB());

			}

		}

	}

	public void setFlou(){

		this.empiler();

		int mR = 0;
		int mG = 0;
		int mB = 0;
		
		Color uneCouleur;
		Color[] desCouleurs;

		BufferedImage tmp = new BufferedImage(imageCourante.getWidth(),
imageCourante.getHeight(), imageCourante.getType());

		for (int i = 0; i < largeur ; i++ ) {

			for (int j = 0; j < hauteur ; j++ ) {
				
				if (i>0 && j>0 && i< (largeur -1) && j< (hauteur -1)) {
					
					mR = 0;
					mG = 0;
					mB = 0;

					desCouleurs = new Color[9];

					desCouleurs[0] = new Color(imageCourante.getRGB(i,j));
					desCouleurs[1] = new Color(imageCourante.getRGB(i+1,j+1));
					desCouleurs[2] = new Color(imageCourante.getRGB(i+1,j));
					desCouleurs[3] = new Color(imageCourante.getRGB(i-1,j));
					desCouleurs[4] = new Color(imageCourante.getRGB(i-1,j-1));
					desCouleurs[5] = new Color(imageCourante.getRGB(i,j+1));
					desCouleurs[6] = new Color(imageCourante.getRGB(i,j-1));
					desCouleurs[7] = new Color(imageCourante.getRGB(i+1,j-1));
					desCouleurs[8] = new Color(imageCourante.getRGB(i-1,j+1));

					for (int k = 0; k < desCouleurs.length ; k++ ) {
						mG = mG + desCouleurs[k].getGreen();
						mR = mR + desCouleurs[k].getRed();
						mB = mB + desCouleurs[k].getBlue();
					}

					mB = (int) mB / 9;
					mG = (int) mG / 9;
					mR = (int) mR / 9;
					uneCouleur = new Color(mR,mG,mB);

				}

				else {
					uneCouleur = new Color(imageCourante.getRGB(i,j));
					
				}

				

				tmp.setRGB(i,j,uneCouleur.getRGB());

			}

		}

		imageCourante = tmp;


	}

	public void setPixelise(){

	}

	public void setNegatif(){

		Color uneCouleur;

		this.empiler();

		for (int i = 0; i < largeur ; i++ ) {

			for (int j = 0; j < hauteur ; j++ ) {
				
				uneCouleur = new Color(imageCourante.getRGB(i,j));

				uneCouleur = new Color(255 - uneCouleur.getRed(),255 - uneCouleur.getGreen(),255 - uneCouleur.getBlue());

				imageCourante.setRGB(i,j,uneCouleur.getRGB());

			}

		}

	}

	public void setSepia(){

	}	

	public void setNet(){

	}

	public void setContraste(){

	}

	public void rogner(){

	}

	public void redimensionner(){

	}

	private void empiler(){
		
		BufferedImage tmp = new BufferedImage(imageCourante.getWidth(),
imageCourante.getHeight(), imageCourante.getType());
		Graphics g = tmp.getGraphics();
		g.drawImage(imageCourante, 0, 0, null);
		g.dispose();

		pileDeModification.push(tmp);
	}

	private void depiler(){
		imageCourante = pileDeModification.pop();
	}

}