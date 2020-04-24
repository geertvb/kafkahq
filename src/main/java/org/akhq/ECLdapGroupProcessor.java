package org.akhq;

import io.micronaut.configuration.security.ldap.context.LdapSearchResult;
import io.micronaut.configuration.security.ldap.group.DefaultLdapGroupProcessor;
import io.micronaut.context.annotation.Replaces;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Singleton;

// this way we don't have to wrestle with LDAP group query.
// in EC ldap the cudgroups are returned in the user object so we can just fetch them from there
@Singleton
@Replaces(DefaultLdapGroupProcessor.class)
public class ECLdapGroupProcessor extends DefaultLdapGroupProcessor {

  @Override public Set<String> getAdditionalGroups(LdapSearchResult result) {
    Set<String> groupSet = new HashSet<>();
    result.getAttributes().get("cudgroup", List.class).ifPresent(groupSet::addAll);
    return groupSet;
  }
}
