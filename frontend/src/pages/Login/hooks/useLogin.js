import usePostAdminLogin from './usePostAdminLogin';

const useAdminLogin = () => {
  const [loginId, setLoginId] = useState('');
  const [password, setPassword] = useState('');

  const [isFocusedId, setIsFocusedId] = useState(false);
  const [isFocusedPw, setIsFocusedPw] = useState(false);
  const { mutate: mutateAdminLogin } = usePostAdminLogin();

  const loginAdmin = (e) => {
    e.preventDefault();

    mutateAdminLogin({
      body: {
        loginId,
        password,
      },
    });
  };

  const getLoginId = (e) => {
    setLoginId(e.target.value);
  };

  const getPassword = (e) => {
    setPassword(e.target.value);
  };

  const handleFocus = (type) => () => {
    switch (type) {
      case 'ID':
        setIsFocusedId(true);
        setIsFocusedPw(false);
        break;
      case 'PW':
        setIsFocusedId(false);
        setIsFocusedPw(true);
        break;
      default:
        break;
    }
  };

  const handleBlur = () => {
    setIsFocusedId(false);
    setIsFocusedPw(false);
  };

  return {
    handleBlur,
    handleFocus,
    isFocusedId,
    isFocusedPw,
    loginAdmin,
    loginId,
    password,
    getLoginId,
    getPassword,
  };
};

export default useAdminLogin;
