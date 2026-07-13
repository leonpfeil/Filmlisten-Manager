package com.sep.server.dbaccess;

import com.sep.server.model.Auth;
import com.sep.server.model.PrivacySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivacySettingsRepository extends JpaRepository<PrivacySettings, String> {
    String getPrivacySettingsByUsername(String username);
}
