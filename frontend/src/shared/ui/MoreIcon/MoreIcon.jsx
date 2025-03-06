import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faEllipsisVertical} from "@fortawesome/free-solid-svg-icons";


export const MoreIcon = () => {

    return (
      <div className = "more-icon">
          <div className="more-icon__wrapper">
              <FontAwesomeIcon icon={faEllipsisVertical} />
          </div>
      </div>

    );

};

export default MoreIcon;