package org.royllo.explorer.web.configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.util.base.Base;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ERROR_MESSAGE_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.UtilPagesConstants.ERROR_403_PAGE;
import static org.royllo.explorer.web.util.constants.UtilPagesConstants.ERROR_404_PAGE;
import static org.royllo.explorer.web.util.constants.UtilPagesConstants.ERROR_500_PAGE;
import static org.royllo.explorer.web.util.constants.UtilPagesConstants.ERROR_PAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Error controller.
 */
@Controller
@RequiredArgsConstructor
public class ErrorConfiguration extends Base implements ErrorController {

    /**
     * Handle error.
     *
     * @param request request
     * @param model   model
     * @return page to display
     */
    @RequestMapping("/error")
    public String handleError(final HttpServletRequest request, final Model model) {

        // We try to get the exception and the error message to display it.
        // TODO This doesn't work - exceptionObject is always null.
        Object exceptionObject = request.getAttribute(ERROR_EXCEPTION);
        if (exceptionObject instanceof Throwable throwable) {
            model.addAttribute(ERROR_MESSAGE_ATTRIBUTE, throwable.getMessage());
        }

        Object status = request.getAttribute(ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == FORBIDDEN.value()) {
                logger.error("Error 403: Forbidden");
                return ERROR_403_PAGE;
            }
            if (statusCode == NOT_FOUND.value()) {
                logger.error("Error 404: Page not found");
                return ERROR_404_PAGE;
            }
            if (statusCode == INTERNAL_SERVER_ERROR.value()) {
                logger.error("Error 500: Server error");
                return ERROR_500_PAGE;
            }
        }
        return ERROR_PAGE;
    }

}
