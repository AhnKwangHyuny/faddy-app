import UserProfile from 'widgets/UserProfile/UserProfile';
import FollowButton from "widgets/Button/FollowButton/FollowButton";
import MoreIcon from "shared/ui/MoreIcon/MoreIcon";

const ProfileFollowAction = ({user}) => {
    return (
        <section className="profile-follow__section">
            <UserProfile nickname = {user.nickname} profile = {user.profile}/>
            <FollowButton writerName ={user.username}/>
            <MoreIcon/>
        </section>
    );

}

export default ProfileFollowAction;