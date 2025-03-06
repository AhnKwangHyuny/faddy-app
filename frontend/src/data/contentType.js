export const ContentTypes = ['snap', 'styleBoard', 'styleBoardComment', 'user'];

export const mapContentTypeToLabel = (contentType) => {

  if (ContentTypes.includes(contentType)) {
    return contentType;
  }

  return null;
};