import styled from 'styled-components';

export const MainContainer = styled.main`
  background-color: #fff;
  display: flex;
  max-width: 480px;
  width: 100%;
  padding-bottom: 29px;
  flex-direction: column;
  margin: 0 auto;
`;

export const HeaderImage = styled.img`
  width: 100%;
  object-fit: cover;
`;

export const Section = styled.section`
  display: flex;
  margin-top: 44px;
  width: 100%;
  flex-direction: column;
  padding: 0 32px;
`;

export const SectionHeader = styled.div`
  display: flex;
  justify-content: space-between;
  gap: 20px;
  font-size: 15px;
  color: #14304a;
  font-weight: 600;
  white-space: nowrap;
  text-align: center;
  letter-spacing: 0.07px;
`;

export const Icon = styled.img`
  width: 13px;
`;

export const Title = styled.h1`
  flex-grow: 1;
  font-family: Inter, sans-serif;
`;

export const Description = styled.p`
  color: rgba(80, 90, 99, 0.8);
  margin-top: 64px;
  font: 500 18px Inter, sans-serif;
`;

export const FormSection = styled.section`
  background-color: rgba(255, 255, 255, 1);
  display: flex;
  margin-top: 55px;
  width: 100%;
  flex-direction: column;
  font-weight: 600;
  padding: 24px 23px 50px;
`;

export const Instruction = styled.div`
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
`;

export const InstructionTitle = styled.h2`
  font-size: 18px;
  color: #14304a;
  text-align: center;
`;

export const Input = styled.input`
  color: #c4c4c4;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid transparent;

  ::placeholder {
    color: #C4C4C4;
  }
`;

export const Button = styled.button`
  border-radius: 8px;
  background-color: rgba(217, 217, 217, 0.7);
  color: #000;
  text-align: center;
  padding: 6px 19px;
  font-size: 14px;
  font-family: Inter, sans-serif;
  margin-top: 34px;
  cursor: pointer;
`;

export const VerificationTitle = styled.h3`
  color: #14304a;
  text-align: center;
  margin-top: 25px;
  font-size: 18px;
`;

export const VerifyButton = styled.button`
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
`;


export const ProgressIndicator = styled.div`
  border-radius: 2.5px;
  border: 1px solid #d9d9d9;
  background-color: rgba(217, 217, 217, 0.85);
  width: 134px;
  height: 5px;
  margin-top: 163px;
  align-self: center;
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

