
function ErrorMessage({ message, onRetry = null }) {
    return (
        <div className="error-container">
            <p>Error: {message}</p>
            {onRetry && (
                <button onClick={onRetry}>Try Again</button>
            )}
        </div>
    );
}

export default ErrorMessage;