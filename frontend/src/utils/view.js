  export const DisplayMessage = ({ idError, idMessage }) => { // message or error 출력
    if (idError) {
      return <Style.ErrorMessage>{idError}</Style.ErrorMessage>;
    } else if (idMessage) {
      return <Style.Message>{idMessage}</Style.Message>;
    }
      return null;
    };