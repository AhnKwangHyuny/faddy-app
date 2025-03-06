import styled from 'styled-components';

export const Instruction = styled.p`
  letter-spacing: 0.14px;
  margin-top: 27px;
  font-size: 28px;
  font-family: Inter, sans-serif;
  text-align: center;
`;

export const InputField = styled.input`
  font-family: Inter, sans-serif;
  font-size: 18px;
  border: none;
  border-bottom: 2px solid #000;
  background-color: transparent;
  margin-top: 12px;
  padding: 8px 0;
  text-align: center;

  ::placeholder {
    color: #888;
  }

  &:focus {
    outline: none;
  }
`;

export const StepInfo = styled.p`
  text-align: center;
  margin-top: 364px;
  white-space: nowrap;
  font-size: 13px;
  font-family: Inter, sans-serif;
`;

export const ProgressSection = styled.section`
  border-radius: 50px;
  background-color: #f0f0f0;
  display: flex;
  margin-top: 14px;
  width: 100%;
  max-width: 327px;
  justify-content: center;
`;

export const ProgressBar = styled.div`
  border-radius: 50px;
  background-color: #000;
  height: 8px;
  width: 50%; /* Example progress */
`;

export const NextButton = styled.button`
  align-items: center;
    border-radius: 32px;
    background-color: ${props => props.disabled ? 'transparent' : '#000'};
    color: #fff;
    text-align: center;
    padding: 19px 60px;
    font-weight: 700;
    font-size: 15px;
    font-family: Inter, sans-serif;
    margin-top: 87px;
    max-width: 327px;
    width: 100%;
    cursor: pointer;
    opacity: ${props => props.disabled ? '0.5' : '1'};

  &:hover {
    background-color: #333;
  }
`;

export const FooterIndicator = styled.footer`
  border-radius: 2.5px;
  border: 1px solid #d9d9d9;
  background-color: rgba(217, 217, 217, 0.85);
  margin-top: 166px;
  width: 134px;
  height: 5px;
`;

export const ErrorMessage = styled.p`
  color: red;
  font-size: 12px;
  margin-top: 5px;
`;

export const Message = styled.p`
  color: green;
  font-size: 12px;
  margin-top: 5px;
`;
