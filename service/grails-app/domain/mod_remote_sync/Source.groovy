package mod_remote_sync

import grails.gorm.MultiTenant;
import mod_remote_sync.source.RemoteSyncActivity;

/**
 *  A "Source" is a remote system that can generate a stream of Resource records. This is the 
 *  base class which sits at the root of all sources, source specifc subclasses define different
 *  implementations - OAISource initially, but maybe FileUploadDirectory, DropBox, etc in the future
 */
public abstract class Source implements MultiTenant<Source> {

  String id
  Authority auth
  String name
  Date dateCreated
  Date lastUpdated
  String emits

  // String SourceVerifiedBy
  // String SourceSignature
  // Refdata status

  static constraints = {
      auth (nullable : false)
     emits (nullable : true)
  }

  static mapping = {
    table 'mrs_source'
    tablePerHierarchy false
    id                     column : 'src_id', generator: 'uuid2', length:36
    version                column : 'src_version'
    dateCreated            column : 'src_date_created'
    lastUpdated            column : 'src_date_updated'
    name                   column : 'src_name'
    auth                   column : 'src_authority_fk'
    emits                  column : 'src_emits'
  }

  public abstract RemoteSyncActivity getActivity();
}
