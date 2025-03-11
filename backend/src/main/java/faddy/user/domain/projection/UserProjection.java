package faddy.user.domain.projection;

import faddy.profile.domain.projection.ProfileProjection;

public interface UserProjection {
    Long getId();
    ProfileProjection getProfile();
    String getUsername();
    String getNickname();
    String getEmail();
}
