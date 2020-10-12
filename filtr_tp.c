/************************************************/
/* Auteur: Franck Luthon (LIUPPA)		*/
/* Revision: 6/01				*/
/* Routines: TP filtrage:lissage,deriv.,contours*/
/************************************************/
#include <bibima.h>

PImage frlu_lissage_expo();
void frlu_lissX();
void frlu_lissY();
void frlu_derivX();
void frlu_derivY();
void frlu_detecontour();
Datatyp frlu_img2histo();
void frlu_histo2img();
PFlot frlu_img2fft();
void frlu_fft();
void frlu_cmult();
void frlu_cmodarg();
/*----------------------------------------------*/
/* filtrage lissage exponentiel structure image	*/
/*----------------------------------------------*/
PImage frlu_lissage_expo(PImage img, Datatyp alpha)
{
	int ligne=img->lin;
	int colonne=img->col;
	PImage out=(PImage)ima_create(ligne,colonne);	/*image sortie*/
	PImage tmp=(PImage)ima_create(ligne,colonne);	/*image tampon*/
	Datatyp b=exp(-alpha);
		
/*printf("b=%g\n",b);*/
	frlu_lissX(img,tmp,b);
	frlu_lissY(tmp,out,b);
	
	ima_destroy(tmp);
	return out;
}
/*----------------------------------------------*/
/* filtrage structure image selon lignes 	*/
/*----------------------------------------------*/
void frlu_lissX(PImage img, PImage out, Datatyp b)
{
	int ligne=img->lin;
	int colonne=img->col;
	Datatyp pixel;
	Datatyp d=(1.-b);
	int l,c;
	
	/*traitement commun de toutes les lignes*/
	for(l=0;l<ligne;l++) {
		/*traitement particulier premier pixel de ligne*/
		c=0;
		pixel =	d * (Datatyp) (img->pix[l][c]);
		out->pix[l][c]=(Pixtyp) pixel;

		/*traitement aller des autres pixels*/	
		for(c=1;c<colonne;c++) {
			pixel	= d * (Datatyp) (img->pix[l][c])
				+ b * (Datatyp) (out->pix[l][c-1]);
			out->pix[l][c]=(Pixtyp) pixel;
		}
	
		/*traitement particulier dernier pixel de ligne*/
		c=colonne-1;
		pixel =	d * (Datatyp) (out->pix[l][c]);
		out->pix[l][c]=(Pixtyp) pixel;

		/*traitement retour des autres pixels*/	
		for(c=colonne-2;c>=0;c--) {
			pixel	= d * (Datatyp) (out->pix[l][c])
				+ b * (Datatyp) (out->pix[l][c+1]);
			out->pix[l][c]=(Pixtyp) pixel;
		}
	}
}
/*----------------------------------------------*/
/* filtrage structure image selon colonnes 	*/
/*----------------------------------------------*/
void frlu_lissY(PImage img, PImage out, Datatyp b)
{
	int ligne=img->lin;
	int colonne=img->col;
	Datatyp pixel;
	Datatyp d=(1.-b);
	int l,c;
	
	/*traitement commun de toutes les lignes*/
	for(c=0;c<colonne;c++) {
		/*traitement particulier premier pixel de colonne*/
		l=0;
		pixel =	d * (Datatyp)(img->pix[l][c]);
		out->pix[l][c]=(Pixtyp) pixel;

		/*traitement aller des autres pixels*/	
		for(l=1;l<ligne;l++) {
			pixel	= d*(Datatyp) (img->pix[l][c])
				+ b*(Datatyp) (out->pix[l-1][c]);
			out->pix[l][c]=(Pixtyp) pixel;
		}
	
		/*traitement particulier dernier pixel de colonne*/
		l=ligne-1;
		pixel =	d * (Datatyp) (out->pix[l][c]);
		out->pix[l][c]=(Pixtyp) pixel;

		/*traitement retour des autres pixels*/	
		for(l=ligne-2;l>=0;l--) {
			pixel	= d*(Datatyp) (out->pix[l][c])
				+ b*(Datatyp) (out->pix[l+1][c]);
			out->pix[l][c]=(Pixtyp) pixel;
		}
	}
}
/*----------------------------------------------*/
/* filtrage structure image selon lignes 	*/
/*----------------------------------------------*/
void frlu_derivX(PImage img, PData out, Datatyp alpha)
{
	int ligne=img->lin;
	int colonne=img->col;
	Datatyp b=exp(-alpha);
	Datatyp d=(1.-b);
	register int l,c;
	PData out1=(PData)data_create(ligne,colonne);	/*tampon*/
	
	/*traitement commun de toutes les lignes*/
	for(l=0;l<ligne;l++) {
		/*traitement particulier premier pixel de ligne*/
		c=0;
		out->pix[l][c]=-d*(Datatyp)(img->pix[l][c]);

		/*traitement aller des autres pixels*/	
		for(c=1;c<colonne;c++)
			out->pix[l][c]=-d*(Datatyp)(img->pix[l][c])+b*(out->pix[l][c-1]);
	
		/*traitement particulier dernier pixel de ligne*/
		c=colonne-1;
		out1->pix[l][c]= d*(Datatyp)(img->pix[l][c]);

		/*traitement retour des autres pixels*/	
		for(c=colonne-2;c>=0;c--)
			out1->pix[l][c]=d*(Datatyp)(img->pix[l][c])+b*(out1->pix[l][c+1]);

		/*somme*/
		for(c=0;c<colonne;c++)
			out->pix[l][c]=out->pix[l][c]+out1->pix[l][c];
	}
	data_destroy(out1);
}
/*----------------------------------------------*/
/* filtrage structure image selon colonnes 	*/
/*----------------------------------------------*/
void frlu_derivY(PImage img, PData out, Datatyp alpha)
{
	int ligne=img->lin;
	int colonne=img->col;
	Datatyp b=exp(-alpha);
	Datatyp d=(1.-b);
	register int l,c;
	PData out1=(PData)data_create(ligne,colonne);	/*tampon*/
	
	/*traitement commun de toutes les lignes*/
	for(c=0;c<colonne;c++) {
		/*traitement particulier premier pixel de colonne*/
		l=0;
		out->pix[l][c]=-d*(Datatyp)(img->pix[l][c]);

		/*traitement aller des autres pixels*/	
		for(l=1;l<ligne;l++)
			out->pix[l][c]=-d*(Datatyp)(img->pix[l][c])+b*(out->pix[l-1][c]);
	
		/*traitement particulier dernier pixel de colonne*/
		l=ligne-1;
		out1->pix[l][c]= d*(Datatyp)(img->pix[l][c]);

		/*traitement retour des autres pixels*/	
		for(l=ligne-2;l>=0;l--)
			out1->pix[l][c]=d*(Datatyp)(img->pix[l][c])+b*(out1->pix[l+1][c]);

		/*somme*/
		for(l=0;l<ligne;l++)
			out->pix[l][c]=out->pix[l][c]+out1->pix[l][c];
	}
	data_destroy(out1);
}
/*----------------------------------------------*/
/* seuillage module gradient pour contours	*/
/*----------------------------------------------*/
void frlu_detecontour(PData gx, PData gy, PData mod, PData phi, Datatyp s, PImage cont)
{
	int ligne=gx->lin;
	int colonne=gx->col;
	register int l,c;
	Datatyp x,y;
	
	for(l=0;l<ligne;l++)
		for(c=0;c<colonne;c++) {
			cont->pix[l][c]=(Pixtyp)255;
			x=gx->pix[l][c];
			y=gy->pix[l][c];
			mod->pix[l][c]=sqrt(pow(x,2)+pow(y,2));
			phi->pix[l][c]=atan2(y,x);
			if(mod->pix[l][c]>s)
				cont->pix[l][c]=(Pixtyp)0;
		}
}
/*----------------------------------------------*/
/* calcul histogramme (ou probas) et entropie	*/
/*----------------------------------------------*/
Datatyp frlu_img2histo(PImage img, Datatyp *out, int choix)
{
	int ligne=img->lin;
	int colonne=img->col;
	int size=ligne*colonne;
	Pixtyp *pin;
	Datatyp Entrop=0.;
	int imax=NBGRIS;
	register int i,l,c;
	
	/*calcul histogramme de l'image*/
	for (i=0;i<imax;i++)
		out[i]=0;		
	for(l=0;l<ligne;l++) {
		pin=img->pix[l];
		for(c=0;c<colonne;c++)
			out[(Pixtyp)(*pin++)]+=1;
	}
	if(choix==0) { /*calcul entropie sans calcul des proba*/
	for (i=0;i<imax;i++)
		Entrop+=out[i]*log(out[i]*(out[i]!=0)+(out[i]==0));
	Entrop=(-Entrop/(Datatyp)(size) +log((Datatyp)(size)))/log(2.);
	}
	else { /*calcul des proba*/
	for (i=0;i<imax;i++)
		out[i]=out[i]/(Datatyp)(size);			
	/*Calcul entropie a partir des proba*/
	for (i=0;i<imax;i++)
		Entrop+=out[i]*log(out[i]*(out[i]!=0)+(out[i]==0));
	Entrop=-Entrop/log(2.);
	}
	return(Entrop);
}
/*----------------------------------------------*/
/* image d'histogramme/proba avec position seuil*/
/*----------------------------------------------*/
void frlu_histo2img(Datatyp *hin, PImage out, int seuil)
{
	int ligne=out->lin;
	int colonne=out->col;
	int imax=NBGRIS;
	Datatyp frac=(Datatyp)(imax)/colonne;
	Datatyp max=0;
	Datatyp pmoy;
	Pixtyp *pout;
	int rap;
	register int i,l,c;
			
	/*Calcul du maximum et normalisation verticale de la courbe*/				
	for (i=0;i<imax;i++)
		max=max*(max>=hin[i])+hin[i]*(max<hin[i]);
	for (i=0;i<imax;i++)
		hin[i]=ligne*hin[i]/max;
	/*Gestion de la taille d'affichage*/
	if(frac<=1)
		rap=1;
	else if(frac<=2)
		rap=2;
	else
		rap=(int)frac;
/*printf("frac=%g\trap=%d\n",frac,rap);*/
	if(seuil>=colonne)
		seuil=colonne-1;
	/*Remplissage de l'image en N&B (avec moyennage si besoin)*/					
	for(l=0;l<ligne;l++) {
		pout=out->pix[l];
		for(c=0;c<imax;c+=rap) {
			pmoy=0;
			for(i=0;i<rap;i++)
				pmoy+=hin[c+i]/rap;
			*pout++=(Pixtyp)(255*((int)(ligne-l)>(int)(pmoy)));
		}
		out->pix[l][seuil]=0;
	}
}
/*----------------------------------------------*/
/* FFT*/
/*----------------------------------------------*/
PFlot frlu_img2fft(PImage img, int padding)
{
	int ligne=img->lin;
	int colonne=img->col;
	Datatyp lbx=log(colonne)/log(2.);
	Datatyp lby=log(ligne)/log(2.);
	int ln=(int)lby; 	
	int cn=(int)lbx;
	int sizex,sizey;
	PCmplx Z;
	PFlot fft;
	PImage temp;
	register int l,c;
	
	/*Zero-padding eventuel*/
	if(padding==1) {
		if(lbx>cn)	/*egalite si dim=puissance de 2*/
			cn=cn+1;
		if(lby>ln)
			ln=ln+1;
	}
	sizex=(int)pow(2,cn);
	sizey=(int)pow(2,ln);
	temp=(PImage)ima_create(sizey,sizex);

	if(padding==1) {
		for(l=0;l<ligne;l++) {
			for(c=0;c<colonne;c++)
				temp->pix[l][c]=img->pix[l][c];
			for(c=colonne;c<sizex;c++)
				temp->pix[l][c]=(Pixtyp)0;
		}
		for(l=ligne;l<sizey;l++)
			for(c=0;c<sizex;c++)
				temp->pix[l][c]=(Pixtyp)0;
	}
	else {
		for(l=0;l<sizey;l++)
			for(c=0;c<sizex;c++)
				temp->pix[l][c]=img->pix[l][c];
	}
	ligne=sizey;
	colonne=sizex;
/*printf("(lbx,lby,cn,ln,C,L)=(%g,%g,%d,%d,%d,%d)\n",lbx,lby,cn,ln,colonne,ligne);*/

/*TEST FFT 8x8*//*ln=3; cn=3;ligne=8; colonne=8;*/
	
	fft=(PFlot)flot_create(ligne,colonne);
	
	/*FFT sur les lignes*/
	Z=(PCmplx)malloc(colonne*sizeof(Cmplx));
	for(l=0;l<ligne;l++){
		for(c=0;c<colonne;c++){
			Z[c].x=(float)temp->pix[l][c];
			Z[c].y=0.;
		}
/*TEST FFT 8x8*//*for(c=0;c<colonne;c++){Z[c].x=0.;Z[c].y=0.;}
if(l==3||l==4){Z[3].x=1.;Z[3].y=0;Z[4].x=1;;Z[4].y=0;}*/
		
		frlu_fft(Z,cn);
		for(c=0;c<colonne;c++){
			fft->vx[l][c]=(Z[c].x)*colonne;
			fft->vy[l][c]=(Z[c].y)*colonne;
		}
	}
	free(Z);

	/*FFT sur les colonnes*/
	Z=(PCmplx)malloc(ligne*sizeof(Cmplx));
	for(c=0;c<colonne;c++){
		for(l=0;l<ligne;l++){
			Z[l].x=fft->vx[l][c];
			Z[l].y=fft->vy[l][c];
		}
		frlu_fft(Z,ln);
		for(l=0;l<ligne;l++){
			fft->vx[l][c]=Z[l].x;
			fft->vy[l][c]=Z[l].y;
		}
	}
	free(Z);
	
/*TEST FFT 8x8*//*for(l=0;l<ligne;l++) for(c=0;c<colonne;c++){
printf("l=%d\tc=%d\tfx=%g\tfy=%g\n",l,c,fft->vx[l][c],fft->vy[l][c]);}*/
	ima_destroy(temp);
	return fft;
}
/*----------------------------------------------*/
/* routine FFT-1D (cf. Gonzalez & Wintz p127)	*/
/*----------------------------------------------*/
void frlu_fft(PCmplx f, int ln)
{
	int n=pow(2,ln);
	PCmplx U=(PCmplx)malloc(sizeof(Cmplx));
	PCmplx w=(PCmplx)malloc(sizeof(Cmplx));
	PCmplx t=(PCmplx)malloc(sizeof(Cmplx));
	register int i,j,k,l,le,le1,ip;
	
	/*permutation des donnees*/
	j=0;
	for(i=0;i<n-1;i++) {
		if(i>=j) goto etic1;
		t->x=f[j].x; t->y=f[j].y;
		f[j].x=f[i].x; f[j].y=f[i].y;
		f[i].x=t->x; f[i].y=t->y;
etic1:		k=n>>1;
etic2:		if(k>j) goto etic3;
		j-=k;
		k=k>>1;
		goto etic2;
etic3:		j+=k;
/*printf("Permut:\ti=%d\tj=%d\tk=%d\n",i,j,k);*/
	}
	/*calculs de FFT-1D*/
	for(l=1;l<=ln;l++) {
		le=pow(2,l);
		le1=le>>1;
		U->x=1.;
		U->y=0.;
		w->x=cos(PI/le1);
		w->y=-sin(PI/le1);
		for(j=0;j<le1;j++) {
			for(i=j;i<n;i+=le) {
				ip=i+le1;
				t->x=(f[ip].x*U->x)-(f[ip].y*U->y);
				t->y=(f[ip].x*U->y)+(f[ip].y*U->x);
				f[ip].x=f[i].x-t->x; f[ip].y=f[i].y-t->y;
				f[i].x=f[i].x+t->x; f[i].y=f[i].y+t->y;
/*printf("Calcul:\ti=%d\tip=%d\n",i,ip);*/
			}
			frlu_cmult(U,w,t);
			U->x=t->x;
			U->y=t->y;
		}
	}
	/*normalisation*/
	for(i=0;i<n;i++) {
		f[i].x=f[i].x/(float)(n);
		f[i].y=f[i].y/(float)(n);
	}		
	free(U);
	free(w);
	free(t);
}
/*----------------------------------------------*/
/* multiplication de 2 nombres complexes	*/
/*----------------------------------------------*/
void frlu_cmult(PCmplx z1, PCmplx z2, PCmplx out)
{
	out->x=z1->x*z2->x-z1->y*z2->y;
	out->y=z1->x*z2->y+z1->y*z2->x;
}
/*----------------------------------------------*/
/* module et argument d'un nombre complexe	*/
/*----------------------------------------------*/
void frlu_cmodarg(PCmplx z)
{
	z->mod=sqrt(z->x*z->x+z->y*z->y);
	z->arg=atan2(z->y,z->x);
}
/******************FIN**************/
