/** @jsxImportSource @emotion/react */

function Button({ children }) {
  return (
    <button
      css={{
        font-family: Source Sans Pro, -apple-system, Roboto, Helvetica, sans-serif;
        justify-content: center;
        align-items: center;
        border-radius: 100px;
        background-color: #000;
        margin-top: 29px;
        color: #fff;
        white-space: nowrap;
        padding: 9px 60px;
      }}
    >
      {children}
    </button>
  );
}

export default Button;
