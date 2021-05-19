package mod_remote_sync

import grails.rest.*
import grails.converters.*

import com.k_int.web.toolkit.settings.AppSetting

import com.k_int.okapi.OkapiTenantAwareController
import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j
import org.olf.rs.workflow.*;
import mod_remote_sync.Source

class SettingController extends OkapiTenantAwareController<AppSetting> {
  
  static responseFormats = ['json', 'xml']
  def sourceRegisterService
  def extractService
  
  SettingController() {
    super(AppSetting)
  }


  def worker() {
    def result = [result:'OK']
    String tenant_header = request.getHeader('X-OKAPI-TENANT')
    log.debug("Worker thread invoked....${tenant_header}");
    // backgroundTaskService.performReshareTasks(tenant_header+'_mod_rs');
    Source.withTransaction {
      extractService.start()
    }
    render result as JSON
  }

  def configureFromRegister() {
    def result = [result:'OK']
    String tenant_header = request.getHeader('X-OKAPI-TENANT')
    log.debug("configureFromRegister");
    log.debug("${request.JSON}");
    if ( ( request.JSON != null ) &&
         ( request.JSON.url != null ) ) {
      Source.withTransaction {
        sourceRegisterService.load(request.JSON.url)
      }
    }
    render result as JSON
  }
}
