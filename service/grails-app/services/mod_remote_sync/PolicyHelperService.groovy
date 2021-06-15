package mod_remote_sync

import grails.gorm.transactions.Transactional
import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.Tenants
import com.k_int.web.toolkit.settings.AppSetting
import com.k_int.web.toolkit.refdata.*
import com.k_int.okapi.OkapiTenantResolver

@Transactional
class PolicyHelperService {

  ResourceMappingService resourceMappingService

  /**
   *  POLICY: Resource creation inside folio is fully controlled by a human observer.
   *
   *    If we have seen this resource before, no further action needed by this policy
   *
   *    On seeing a new resource in an upstream system, an operator needs to tell the system if
   *           a) A new resource should be created inside the target system
   *           b) The new resource should be mapped to (And used to update) an existing resource in the system
   *           c) The new resource should never be mapped
   *
   */
  public boolean manualResourceMapping(String source,
                                       String resource_id,
                                       String mapping_context,
                                       String target_context,
                                       Map local_context) {

    log.debug("PolicyHelperService::manualResourceMapping(${source},${resource_id},${mapping_context},${target_context},${local_context})");

    boolean result = false;

    ResourceMapping rm = resourceMappingService.lookupMapping(source, resource_id, mapping_context)

    if ( rm != null ) {
      // The resource is known to us - continue
      result = true;
    }
    else {
      // Unknown - fail - here we should check the "ImportKB" to see if we have already been told what to do
      // in this circumstance
      result = false; 
    }

    return result;
  }
}
