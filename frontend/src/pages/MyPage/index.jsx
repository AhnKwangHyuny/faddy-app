import { useNavigate } from 'react-router-dom';

import { AiOutlineUnorderedList, AiOutlineLogout, AiOutlineFileText } from 'react-icons/ai';
import { GrUserSettings } from 'react-icons/gr';


// 아이콘 배열에 실제 아이콘 컴포넌트를 할당
const ICONS = [
    <AiOutlineUnorderedList />,
    <AiOutlineFileText />,
    <GrUserSettings />,
    <AiOutlineLogout />,
];

const MYPAGE_NAV_OPTIONS = [
    { key: 'rewardHistory', value: '리워드 사용 내역' },
    { key: 'stampHistory', value: '스탬프 적립 내역' },
    { key: 'customerSetting', value: '내 정보 변경' },
    { key: 'feedback', value: '서비스 만족도 조사' },
    { key: 'logout', value: '로그아웃' },
];

const MyPage = () => {
    const navigate = useNavigate();
    const { customerProfile } = useCustomerProfile();

    const navigatePage = (key: string) => () => {
        if (key === 'logout') {
            // TODO: 완전한 로그아웃 구현해야 함.
            localStorage.setItem('login-token', '');
            navigate(ROUTER_PATH.login);
            return;
        }
        navigate(ROUTER_PATH[key]);
    };

    const isFeedBack = (key: string) => key === 'feedback';

    return (
        <>
            <NicknameContainer>
                <Nickname>{customerProfile?.profile.nickname}님</Nickname>
            </NicknameContainer>
            <NavContainer>
                {MYPAGE_NAV_OPTIONS.map((option, index) => (
                    <NavWrapper key={option.key} onClick={navigatePage(option.key)}>
                        {ICONS[index]}
                        <span>{option.value}</span>
                    </NavWrapper>
                ))}
            </NavContainer>
        </>
    );
};

export default MyPage;
