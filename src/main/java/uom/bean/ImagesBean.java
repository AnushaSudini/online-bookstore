/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uom.bean;

import java.io.ByteArrayInputStream;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Anusha
 */

@ManagedBean
@ApplicationScoped
public class ImagesBean {
    
    public StreamedContent getImage() {
        if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("imageID");
            List<StreamedContent> ct = (List<StreamedContent>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("img");
            return ct.get(Integer.valueOf(id));
            //return new DefaultStreamedContent(new ByteArrayInputStream(dto.getImage()));
        }
    }
}
