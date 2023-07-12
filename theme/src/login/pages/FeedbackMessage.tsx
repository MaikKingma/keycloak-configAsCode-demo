// Component 1: The Container
export default function FeedbackMessageContainer(props: { children: any }) {
  const { children } = props
  return <div>{children}</div>
}

// Component 2: The Alert Icon
function AlertIcon({ type }: { type: string }) {
  return <span>{type}</span>
}

// Component 3: The Message Text
function MessageText({ summary }: { summary: string }) {
  return (
    <span
      className="kc-feedback-text"
      dangerouslySetInnerHTML={{
        __html: summary,
      }}
    />
  )
}

//
// <FeedbackMessageContainer>
//   <AlertIcon type={message.type} />
//   <MessageText summary={message.summary} />
// </FeedbackMessageContainer>

// Utility function to capitalize first letter
function capitalize(str: string) {
  return str.charAt(0).toUpperCase() + str.slice(1)
}
