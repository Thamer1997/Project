package Main;

import java.util.Vector;


public class Ordenancement {
   //******** => FIFO
    public void FCFS(Processus[]tab)
    {
        Processus aux;
        int min;
        //Tri par temps arrivé
        for(int i=0;i<tab.length;i++){
            min=i;           
            for(int j=i+1;j<tab.length;j++)
            {if(tab[j].tempsArr<tab[min].tempsArr)
                {min=j;}}
            if(min!=i)
            {aux=tab[i];
            tab[i]=tab[min];
            tab[min]=aux;
            }}
        //Priorité
        boolean test=false;
        while(test==false){
        for(int i=0;i<tab.length-1;i++)
        { if((tab[i].tempsArr==tab[i+1].tempsArr)&&(tab[i].priorite!=0))
            { aux=tab[i];
              tab[i]=tab[i+1];
              tab[i+1]=aux;
              test=false;}
            else
            {test=true;}}
        }}
    
    
       //Les processus disponible a l'instant x
        public boolean test_temps_arr(Processus[] tab, int x)
        {
            for(int i=0;i<tab.length;i++)
            {
               if(tab[i].tempsArr<=x)
               {
                   return true;
               }
               
            }
            return false;
        }
        
      
    
    //******* => SJF Non premptif
        public Processus[] SJF_NP(Processus []tab)
    {
        int second=0;//le chronometre
        
        //v2 Processus executé
        Vector<Processus> v2 = new Vector<Processus>();
        //v3 processus dispo
        Vector<Processus> v3 = new Vector<Processus>();
        //tab2 resultat
        Processus [] tab2= new Processus[tab.length];
        int z=0;//indice tab2
        int i=0; //nbre iteration
        
        
       
       
        while(i<tab.length)//tant que il ya des processus ne sont pas encore executé
        {
            
            if(test_temps_arr(tab,second))
            {
                //les processus qui sont disponible a l'instant second nous les met dans v3
             for(int j=0;j<tab.length;j++)
             {
                 if(tab[j].tempsArr <= second && v2.contains(tab[j])==false)
                 {
                     v3.add(tab[j]);
                 }
             }
             
             //choisir le min duréeEx parmi les processus disponibles
             Processus min = v3.elementAt(0);
             for(int j=1;j<v3.size();j++)
             {
                 if(v3.elementAt(j).dureeEx<min.dureeEx)
                 {
                     min=v3.elementAt(j);
                     
                 }
             }
             
             v2.add(min); //processus executé
             tab2[z]=min; 
             v3.remove(min); 
             second=second+min.dureeEx;
             z++;//indice du tableau
             i++;//condition du while
             
            }
            //dans le cas il n'ya pas de processus disponible a l'instant second
            else{
                //isEmpty:vide
                if(v3.isEmpty()){
                    second++;
                }
                //v3 n'est pas vide il ya d'autre processus disponible et ne sont pas executé
                else
                {
                     Processus min = v3.elementAt(0);
                     for(int j=1;j<v3.size();j++)
                     {
                         if(v3.elementAt(j).dureeEx<min.dureeEx)
                         {
                             min=v3.elementAt(j);

                         }
                     }

                     v2.add(min);
                     tab2[z]=min;
                     v3.remove(min);
                     second=second+min.dureeEx;
                     z++;
                     i++;
                }
            }
            
        }
        
       return tab2;
    }
        
        
            //******* => SJF premptif
        public Vector<Processus> SJF_P(Processus []tab)
    {int second=0;//le chronometre
        //v2 Processus executé
        Vector<Processus> v2 = new Vector<Processus>();
        //v3 processus dispo
        Vector<Processus> v3 = new Vector<Processus>();
        //v4  resultat
        Vector<Processus> v4 = new Vector<Processus>();
        //v5  en cours d'execution
        Vector<Processus> v5 = new Vector<Processus>();
        Processus [] tab2= new Processus[tab.length];
        int z=0;//indice tab2
        int i=0; //nbre iteration
        while(i<tab.length)//tant que il ya des processus ne sont pas encore 
            //executé
        {if(test_temps_arr(tab,second))
            {//les processus qui sont disponible a l'instant second nous les 
                //met dans v3
             for(int j=0;j<tab.length;j++)
             {if(tab[j].tempsArr <= second && v2.contains(tab[j])==false && 
                     v3.contains(tab[j])==false)
                 {v3.add(tab[j]);}}
             //choisir le min duréeEx parmi les processus disponibles
             Processus min = v3.elementAt(0);
             for(int j=1;j<v3.size();j++)
             {if(v3.elementAt(j).dureeEx<min.dureeEx)
                 {min=v3.elementAt(j);}}
             v5.add(min); //processus executé
             tab2[z]=min; 
             v3.remove(min); 
             second++;
             min.dureeEx--;
             
             if(min.dureeEx==0)
             {v2.add(min);
              v4.add(min);
              v5.remove(min);
              i++;}
             else
             {v3.add(min);
                v4.add(min);
                v5.remove(min);}
            }
            //dans le cas il n'ya pas de processus disponible a l'instant second
            else{
                //isEmpty:vide
                if(v3.isEmpty()){second++;}
                //v3 n'est pas vide il ya d'autre processus disponible et ne 
                //sont pas executé
                else
                {Processus min = v3.elementAt(0);
                    for(int j=1;j<v3.size();j++)
                    {if(v3.elementAt(j).dureeEx<min.dureeEx)
                            {min=v3.elementAt(j);}}
                     v5.add(min); //processus executé
                     tab2[z]=min; 
                     v3.remove(min); 
                     second++;
                     min.dureeEx--;
                     if(min.dureeEx==0)
                     {v2.add(min);
                      v4.add(min);
                      v5.remove(min);
                      i++;}
                     else{
                         v3.add(min);
                         v4.add(min);
                         v5.remove(min);}}}}
       return v4;}
        
        
                //******* => RR
       
public void triParTmpArr(Processus[] tab)
{
    for(int i=0;i<tab.length;i++)
    {
        int minTArr=i;
        for(int j=i+1;j<tab.length;j++)
        {
            if(tab[j].tempsArr<tab[minTArr].tempsArr)
            {
                minTArr=j;
            }
            
        }
        
        if(minTArr!=i){
            Processus aux = tab[i];
            tab[i] = tab[minTArr];
            tab[minTArr] = aux;
        
            
        }
    }
}      

    Vector<Processus> RR(Processus[] tab, int quantum) {
        throw new UnsupportedOperationException("Not supported yet."); 
//To change body of generated methods, choose Tools | Templates.
    }
}
