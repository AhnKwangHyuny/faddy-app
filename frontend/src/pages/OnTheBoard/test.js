
const BottomNav = () => {
  return (
    <BottomNavRoot>
      <VectorParent>
        <FrameChild alt="" src="/rectangle-1735.svg" />
        <FrameParent>
          <InstaWrapper>
            <InstaIcon alt="" src="/insta.svg" />
          </InstaWrapper>
          <FrameItem />
          <B>스냅</B>
        </FrameParent>
        <FrameWrapper>
          <FrameGroup>
            <InstaWrapper>
              <InstaIcon alt="" src="/chat-alt.svg" />
            </InstaWrapper>
            <FrameItem />
            <B>피드</B>
          </FrameGroup>
        </FrameWrapper>
        <FrameContainer>
          <GroupIcon loading="lazy" alt="" src="/group-3215@2x.png" />
        </FrameContainer>
        <FrameDiv>
          <FavouriteInstanceParent>
            <InstaWrapper>
              <FavoriteIcon alt="" src="/favorite.svg" />
            </InstaWrapper>
            <RectangleDiv />
            <B1>팔로잉</B1>
          </FavouriteInstanceParent>
          <RectangleParent>
            <RectangleDiv />
            <MessageWrapper>
              <Message>
                <MessageChild />
                <FollowingAndMessages
                  alt=""
                  src="/following-and-messages.svg"
                />
              </Message>
            </MessageWrapper>
            <B1>메세지</B1>
          </RectangleParent>
        </FrameDiv>
      </VectorParent>
    </BottomNavRoot>
  );
};

export default BottomNav;