package au.gov.nsw.records.digitalarchive.model;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPathwaysByInput_format_descLike", "findPathwaysByInput_format_descLikeAndTarget_format_descLike" })
public class Pathway {

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 10)
    private String input_format = null;

    private String input_format_desc;

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 10)
    private String preservation_tool = null;

    private String preservation_tool_desc;

    private String preservation_class;

    @Size(max = 1024)
    private String external_pathway;

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 10)
    private String target_format = null;

    private String target_format_desc;

    @Size(max = 1024)
    private String description;

    private String author;

    private Boolean deprecated = Boolean.FALSE;

    private String deprecated_by = null;

    @Size(max = 1024)
    private String deprecation_desc;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date deprecation_date;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date creation_date = new Date();

    @ManyToOne
    private au.gov.nsw.records.digitalarchive.model.Pathway superseded_by = null;

    @ManyToOne
    private Purpose purpose;
    
    public static TypedQuery<Pathway> findPathwaysByInput_format_descLikeAndTarget_format_descLike(String input_format_desc, String target_format_desc) {
      if (input_format_desc == null || input_format_desc.isEmpty()){
      	input_format_desc = "*";
      }
      input_format_desc = input_format_desc.replace('*', '%');
      if (input_format_desc.charAt(0) != '%') {
          input_format_desc = "%" + input_format_desc;
      }
      if (input_format_desc.charAt(input_format_desc.length() - 1) != '%') {
          input_format_desc = input_format_desc + "%";
      }
      if (target_format_desc == null || target_format_desc.isEmpty()){
      	target_format_desc = "*";
      }
      target_format_desc = target_format_desc.replace('*', '%');
      if (target_format_desc.charAt(0) != '%') {
          target_format_desc = "%" + target_format_desc;
      }
      if (target_format_desc.charAt(target_format_desc.length() - 1) != '%') {
          target_format_desc = target_format_desc + "%";
      }
      EntityManager em = Pathway.entityManager();
      TypedQuery<Pathway> q = em.createQuery("SELECT o FROM Pathway AS o WHERE LOWER(o.input_format_desc) LIKE LOWER(:input_format_desc)  AND LOWER(o.target_format_desc) LIKE LOWER(:target_format_desc)", Pathway.class);
      q.setParameter("input_format_desc", input_format_desc);
      q.setParameter("target_format_desc", target_format_desc);
      return q;
  }
  
  public static TypedQuery<Pathway> findByPuid(String puid) {
      if (puid == null || puid.isEmpty()){
      	puid = "*";
      }
      puid = puid.replace('*', '%');
      if (puid.charAt(0) != '%') {
          puid = "%" + puid;
      }
      if (puid.charAt(puid.length() - 1) != '%') {
          puid = puid + "%";
      }
   
      EntityManager em = Pathway.entityManager();
      
      TypedQuery<Pathway> q = em.createQuery("SELECT o FROM Pathway AS o WHERE LOWER(o.input_format) LIKE LOWER(:puid)", Pathway.class);
      q.setParameter("puid", puid);
      return q;
  }
    
  public static TypedQuery<Pathway> findByPuidAndPurpose(String puid, String purpose) {
  		
      EntityManager em = Pathway.entityManager();
      TypedQuery<Pathway> q = em.createQuery("SELECT pathway FROM Pathway pathway left join pathway.purpose purpose WHERE pathway.input_format = LOWER(:puid) and purpose.name = LOWER(:purpose)", Pathway.class);
      q.setParameter("puid", puid);
      q.setParameter("purpose", purpose);
      return q;
  }
}
