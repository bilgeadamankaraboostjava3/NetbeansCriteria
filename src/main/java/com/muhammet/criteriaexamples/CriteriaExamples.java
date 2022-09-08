package com.muhammet.criteriaexamples;

import com.muhammet.criteriaexamples.repository.entity.AddressType;
import com.muhammet.criteriaexamples.repository.entity.Adres;
import com.muhammet.criteriaexamples.repository.entity.Musteri;
import com.muhammet.criteriaexamples.repository.entity.Person;
import com.muhammet.criteriaexamples.repository.entity.Phone;
import com.muhammet.criteriaexamples.repository.entity.PhoneType;
import com.muhammet.criteriaexamples.utility.HibernateUtility;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CriteriaExamples {
        private static Session ss ;
        private static Transaction tt ;
        private static EntityManager entityManager;
        private static CriteriaBuilder builder;
        
    public static void main(String[] args) {
        ImplData();
        /**
         * BUNLARI SİLME
         */
        entityManager = HibernateUtility.getFACTORY().createEntityManager();
        builder = entityManager.getCriteriaBuilder();
        /**
         * 
         */ 
        //usingParameter("ad","%i%");
      //  usingGroup();
       // nativeQueryGetMusteriList().forEach(System.out::println);
       //namedQueryFindByIdMusteri();
        namedQueryFindAllPagination(1, 10);
    }
    /***
     * SQL  -> select * from tblmusteri
     * JPQL -> SELECT m FROM MUSTERI m
     * HQL  -> FROM MUSTERI
     * 
     * SQL  -> select ad,soyad from tblmusteri where ad like 'K%'
     * JPQL -> SELECT m.ad,m.soyad FROM MUSTERI m WHERE m.ad like 'K%'
     * HQL  -> SELECT m.ad,m.soyad FROM MUSTERI m WHERE m.ad LIKE 'K%'
     * 
     * SQL  -> select * from tblmusteri where ad like 'K%'
     * JPQL -> SELECT m.ad,m.soyad FROM MUSTERI m WHERE m.ad like 'K%'
     * HQL  -> FROM MUSTERI m WHERE m.ad LIKE 'K%'
     * 
     * @return 
     */
    
        public static void namedQueryFindAllPagination(int Page, int Size){
            TypedQuery<Musteri> typedQuery = entityManager.createNamedQuery("Musteri.findAll",Musteri.class);
            typedQuery.setMaxResults(Size);
            typedQuery.setFirstResult(Page*Size);
            List<Musteri> mList = typedQuery.getResultList();
            mList.forEach(System.out::println);
        }
    
        public static void namedQueryFindByIdMusteri(){
             TypedQuery<Musteri> query = entityManager.createNamedQuery("Musteri.findById", Musteri.class);
             query.setParameter("id", 7L);
             List<Musteri> mList = query.getResultList();
             mList.forEach(System.out::println);
        }
    
        public static void namedQueryFindAllMusteri(){
             TypedQuery<Musteri> query = entityManager.createNamedQuery("Musteri.findAll", Musteri.class);
             List<Musteri> mList = query.getResultList();
             mList.forEach(System.out::println);
        }
    
        public static List<Musteri> nativeQueryGetMusteriList(){
            /**
             * Eğer native sorgu kullanırken, dönüş tipi için bir dönüşüm belirtmez iseniz.
             * dönen değer List<Object> olarak döner
             * entityManager.createNativeQuery("select * from tblmusteri").getResultList();
             */            
            
            return entityManager.createNativeQuery("select * from tblmusteri",Musteri.class).getResultList();         
        }

        public static void usingGroup(){
            /**
             * select ad,COUNT(*)from tblmusteri group by ad
             * select ad,SUM(odemeler) tblsatis group by ad
             */
            CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);
            Root<Musteri> root = criteria.from(Musteri.class);
            /**
             * group yapacağımız alanı belirtiyoruz.
             * multiselect ile de grup yapılan alanı ve sayılacak kayıt ı belirtiyoruz.
             *  (count(*)) için builder.count(root) verilirken
             *  (sum([alanadı])) için  builder.sum(root.get([alanadı]))
             */
            
            
            criteria.groupBy(root.get("ad"));
            criteria.multiselect(root.get("ad"),builder.count(root));
            // criteria.multiselect(root.get("ad"),builder.sum(root.get("odemeler")));
            List<Tuple> list = entityManager.createQuery(criteria).getResultList();
            list.forEach(x->{
                
            });
        }
    
        public static void usingParameter(String kolon, String criteriaSoyad){
            /***
             * select * from tblmusteri WHERE soyad like 'K%'
             */
            CriteriaQuery<Musteri> criteria = builder.createQuery(Musteri.class);
            Root<Musteri> root = criteria.from(Musteri.class);
            /**
             *  criteria.where(builder.like(root.get("soyad"),criteriaSoyad));
             */
            ParameterExpression<String> parameter = builder.parameter(String.class);                    
            criteria.where(builder.like(root.get(kolon),parameter));
            TypedQuery<Musteri> typedQuery = entityManager.createQuery(criteria);
            typedQuery.setParameter(parameter,criteriaSoyad);
            List<Musteri> list = typedQuery.getResultList();
            list.forEach(System.out::println);
        }
    
    
        /**
         * DOKÜMANTASYON OKUYALIM
         */
        private static void usingJoin(){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Phone> criteria = builder.createQuery(Phone.class);
        Root<Phone> root = criteria.from(Phone.class);

        // Phone.person is a @ManyToOne
        Join<Phone, Person> personJoin = root.join("person");
        // Person.addresses is an @ElementCollection
        Join<Person, String> addressesJoin = personJoin.join("addresses");

        List<Phone> phones = entityManager.createQuery(criteria).getResultList();
        phones.forEach(System.out::println);
        
        }
        
        private static void deneme(){
          List<Object[]> listem = entityManager.createNativeQuery(""
                  + "select tblmusteri.ad, tbladres.tanim from tblmusteri left join tbladres on tblmusteri.adres_id=tbladres.id").getResultList();
          listem.forEach(x->{
              System.out.print("Müşteri adi...: "+ x[0]);
              System.out.println(" - Müşteri adresi...: "+ x[1]);
              
          });
        }
       
        private static void multipleWhereRoot(){
        CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);
        Root<Musteri> rootMusteri = criteria.from(Musteri.class);
        Root<Adres> rootAdres = criteria.from(Adres.class);
        criteria.multiselect(rootAdres,rootMusteri);
        /**
         * where ad='İŞ' AND tanim like '%M%'
         */
        Predicate predicateAdres = builder.and(
                builder.equal(rootAdres.get("ad"), "İŞ"),
                builder.like(rootAdres.get("tanim"), "%M%")                
        );
        criteria.where(builder.and(predicateAdres));
        
        List<Tuple> tuples =  entityManager.createQuery(criteria).getResultList();
        tuples.forEach(x->{
            System.out.println("x..: "+ x.get(0));
            System.out.println("x..: "+ x.get(1));
            
        });
    }
    
    private static void multipleRoot(){
        CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);
        Root<Musteri> rootMusteri = criteria.from(Musteri.class);
        Root<Adres> rootAdres = criteria.from(Adres.class);
        criteria.multiselect(rootMusteri,rootAdres);
        List<Tuple> tuples =  entityManager.createQuery(criteria).getResultList();
        tuples.forEach(x->{
            System.out.println("x..: "+ x.get(0));
            System.out.println("x..: "+ x.get(1));
            
        });
    }
    
    private static void usingTuple(){
        CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);
        Root<Musteri> root = criteria.from(Musteri.class);
        Path<Long> idlerim = root.get("id");
        Path<String> adlarim = root.get("ad");
        criteria.multiselect(idlerim,adlarim);
        List<Tuple> tuples = entityManager.createQuery(criteria).getResultList();
        tuples.forEach(x->{
            System.out.print("id...: "+ x.get(idlerim));
            System.out.println("- adlarım...: "+ x.get(1));
        });
    }
    
    public static void selectManyColumn(){
        /**
         * select id,ad from tblmusteri
         */
        CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);
        Root<Musteri> root = criteria.from(Musteri.class);
        /**
         * Birden fazla alanı işlemek için Path kullanılır ve array olarak 
         * select e verilir.
         */
        Path<Long> idPath = root.get("id");
        Path<String> adPath = root.get("ad");
        criteria.select(builder.array(idPath,adPath));
        List<Object[]> idAdList = entityManager.createQuery(criteria).getResultList();
        idAdList.forEach(x->{
            System.out.println("x...: "+ x);
            System.out.println(x[0]+" - "+x[1]);
        });
    }
    
    private static void findAll(){
        // select * from tblmusteri
        /**
         * Hangi tablo ile çalışacağız onu belirliyoruz.
         */
        CriteriaQuery<Musteri> criteria = builder.createQuery(Musteri.class);
        /**
         * için kullanılacak kodlama, burada istediğimiz bir alanı seçme imkanı olacaktır.
         */
        Root<Musteri> root = criteria.from(Musteri.class);
        criteria.select(root);
        List<Musteri> mlist = entityManager.createQuery(criteria).getResultList();
        mlist.forEach(System.out::println);
    }
    
    private static void selectOneColumn(){
        // select ad from tblmusteri 
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<Musteri> root = criteria.from(Musteri.class);
        /**
         * select * -> root   -> tablonun tamamı liste olarak, Müşteri listesi
         * select ad -> root.get("ad") -> tek bir kolon, ad listesi
         */
        criteria.select(root.get("ad"));
        List<String> adList = entityManager.createQuery(criteria).getResultList();
        adList.forEach(System.out::println);
    }
        
    private static void ImplData(){
        ss = HibernateUtility.getFACTORY().openSession();
        tt = ss.beginTransaction();
        Adres adres1 = new Adres("EV", "Ankarada bir yer");
        Adres adres2 = new Adres("İŞ", "İzmir merkez");
        Adres adres3 = new Adres("OKUL", "İstanbul");
        Adres adres4 = new Adres("EV", "Antalya");
        Adres adres5 = new Adres("İŞ", "Muğla");
      //  Adres adres6 = new Adres("EV", "Muğla");
        
        for(int i=0;i<10_000;i++){
        Musteri musteri1 = new Musteri("Ahmet"+i, "HOŞ", adres1);
        Musteri musteri2 = new Musteri("Canan"+i, "DEMİR", adres2);
        Musteri musteri3= new Musteri("Bahar"+i, "KUŞ", adres3);
        Musteri musteri4= new Musteri("Deniz"+i, "KUM", adres4);
        Musteri musteri5= new Musteri("Beril"+i, "GÜMÜŞ", adres5);
        Musteri musteri6= new Musteri("Beril"+i, "GÜMÜŞ", adres5);
        Musteri musteri7= new Musteri("Bahar"+i, "GÜMÜŞ", adres5);
        
        ss.save(musteri1);
        ss.save(musteri2);
        ss.save(musteri3);
        ss.save(musteri4);
        ss.save(musteri5);
        ss.save(musteri6);
        ss.save(musteri7);
        }
        tt.commit();
        ss.close();
    }
    
}
