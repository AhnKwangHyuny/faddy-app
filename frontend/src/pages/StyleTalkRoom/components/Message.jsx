import React from 'react';

const Message = ({ children, model }) => {

    if (model.type === 'system' || model.type === 'timestamp') {
        return (
            <div className={`message message-${model.type}`}>
                {children}
            </div>
        );
    }

   const messageClass = model.direction === 'outgoing' ? 'message-outgoing' : 'message-incoming';

   return (
       <div className={`message ${messageClass}`}>
           {children}
       </div>
   );
};

export default Message;