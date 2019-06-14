package uni.tukl.cs.cps.agilaserver.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import lombok.*;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import uni.tukl.cs.cps.agilaserver.AgilaServerApplication;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class AgilaApiError {

	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;
	private List<AgilaApiSubError> subErrors;

	public static ResponseEntity<Object> CreateErrorResponse(List<FieldError> fieldErrors) {
		AgilaApiError apiError = new AgilaApiError(HttpStatus.UNPROCESSABLE_ENTITY);
		apiError.addValidationErrors(fieldErrors);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	private AgilaApiError() {
		timestamp = LocalDateTime.now();
	}

	AgilaApiError(HttpStatus status) {
		this();
		this.status = status;
	}

	AgilaApiError(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	public AgilaApiError(HttpStatus status, String message, Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	private void addSubError(AgilaApiSubError subError) {
		if (subErrors == null) {
			subErrors = new ArrayList<>();
		}
		subErrors.add(subError);
	}

	private void addValidationError(String object, String field, Object rejectedValue, String message, String errorCode) {
		addSubError(new AgilaApiValidationError(object, field, rejectedValue, message, errorCode));
	}

	private void addValidationError(String object, String message, String errorCode) {
		addSubError(new AgilaApiValidationError(object, message, errorCode));
	}

	private void addValidationError(FieldError fieldError) {
		this.addValidationError(
				fieldError.getObjectName(),
				fieldError.getField(),
				fieldError.getRejectedValue(),
				context.getMessage(fieldError, Locale.US),
				fieldError.getCode());
	}

	void addValidationErrors(List<FieldError> fieldErrors) {
		fieldErrors.forEach(this::addValidationError);
	}

	private static AnnotationConfigApplicationContext context =
			new AnnotationConfigApplicationContext(AgilaServerApplication.class);

	private void addValidationError(ObjectError objectError) {
		this.addValidationError(
				objectError.getObjectName(),
				context.getMessage(objectError, Locale.US),
				objectError.getCode());
	}

	void addValidationError(List<ObjectError> globalErrors) {
		globalErrors.forEach(this::addValidationError);
	}

	/**
	 * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
	 * @param cv the ConstraintViolation
	 */
	private void addValidationError(ConstraintViolation<?> cv) {
		this.addValidationError(
				cv.getRootBeanClass().getSimpleName(),
				((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
				cv.getInvalidValue(),
				cv.getMessage(),
				cv.getMessageTemplate());
	}

	void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
		constraintViolations.forEach(this::addValidationError);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage(){ return message; }

	public List<AgilaApiSubError> getSubErrors() { return subErrors; }

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	abstract class AgilaApiSubError {

	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public class AgilaApiValidationError extends AgilaApiSubError {

		private String object;
		private String field;
		private Object rejectedValue;
		private String message;
		private String errorCode;

		AgilaApiValidationError(String object, String message) {
			this.object = object;
			this.message = message;
		}

		AgilaApiValidationError(String object, String message, String errorCode) {
			this(object, message);
			this.errorCode = errorCode;
		}

		AgilaApiValidationError(String object, String field, Object rejectedValue, String message) {
			this(object, message);
			this.field = field;
			this.rejectedValue = rejectedValue;
		}

		AgilaApiValidationError(String object, String field, Object rejectedValue, String message, String errorCode) {
			this(object, field, rejectedValue, message);
			this.errorCode = errorCode;
		}

		public String getObject() {
			return object;
		}

		public void setObject(String object) {
			this.object = object;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public Object getRejectedValue() {
			return rejectedValue;
		}

		public void setRejectedValue(Object rejectedValue) {
			this.rejectedValue = rejectedValue;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getErrorCode() {  return errorCode; }

		public void setErrorCode(String errorCode) {  this.errorCode = errorCode; }

	}
}

class LowerCaseClassNameResolver extends TypeIdResolverBase {

	@Override
	public String idFromValue(Object value) {
		return value.getClass().getSimpleName().toLowerCase();
	}

	@Override
	public String idFromValueAndType(Object value, Class<?> suggestedType) {
		return idFromValue(value);
	}

	@Override
	public JsonTypeInfo.Id getMechanism() {
		return JsonTypeInfo.Id.CUSTOM;
	}
}