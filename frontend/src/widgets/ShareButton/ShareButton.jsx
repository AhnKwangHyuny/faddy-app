import {faShareFromSquare} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const ShareButton = () => {

    return (
      <div className= "share-button">
          <div className="share-button__wrapper">
              <div className="share-button__icon">
                    <span className="material-icons">
                        share
                    </span>
              </div>

          </div>
      </div>
    );
}

export default ShareButton;