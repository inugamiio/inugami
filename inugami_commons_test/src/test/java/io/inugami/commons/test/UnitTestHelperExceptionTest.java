package io.inugami.commons.test;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;
import io.inugami.api.exceptions.UncheckedException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class UnitTestHelperExceptionTest {

    @Test
    public void throwException_default_method() {
        //------------------------------------------
        UnitTestHelperException.assertThrows(() -> {
            throw new UncheckedException();
        });
        //------------------------------------------

        try {
            UnitTestHelperException.assertThrows(() -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

    }


    @Test
    public void throwException_withErrorMessage() {
        //------------------------------------------

        final String msg = null;
        UnitTestHelperException.assertThrows(msg, () -> {
            throw new UncheckedException();
        });

        UnitTestHelperException.assertThrows("this method should throws", () -> {
            throw new UncheckedException("this method should throws");
        });

        //------------------------------------------

        try {
            UnitTestHelperException.assertThrows("this method should throws", () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            UnitTestHelperException.assertThrows("this method should throws", () -> {
                throw new UncheckedException("some error");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("current : \"some error\"  ref: \"this method should throws\"");
        }
    }


    @Test
    public void throwException_withClass() {
        //------------------------------------------

        final String msg = null;
        UnitTestHelperException.assertThrows(UncheckedException.class, () -> {
            throw new UncheckedException();
        });
        //------------------------------------------

        try {
            UnitTestHelperException.assertThrows(UncheckedException.class, () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            UnitTestHelperException.assertThrows(UncheckedException.class, () -> {
                throw new NullPointerException("some error");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("error class isn't a io.inugami.api.exceptions.UncheckedException");
        }
    }


    @Test
    public void throwException_withClassAndMessage() {
        //------------------------------------------

        final String msg = null;
        UnitTestHelperException.assertThrows(UncheckedException.class, "some error", () -> {
            throw new UncheckedException("some error");
        });
        //------------------------------------------

        try {
            UnitTestHelperException.assertThrows(UncheckedException.class, "some error", () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            UnitTestHelperException.assertThrows(UncheckedException.class, "some error", () -> {
                throw new NullPointerException("some error");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("error class isn't a io.inugami.api.exceptions.UncheckedException");
        }

        try {
            UnitTestHelperException.assertThrows(UncheckedException.class, "some error", () -> {
                throw new UncheckedException("sorry");
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("current : \"sorry\"  ref: \"some error\"");
        }
    }


    @Test
    public void throwException_withErrorCode() {
        //------------------------------------------
        final ErrorCode undefinedError = DefaultErrorCode.buildUndefineError();
        final String    msg            = null;
        UnitTestHelperException.assertThrows(undefinedError, () -> {
            throw new UncheckedException(undefinedError);
        });
        //------------------------------------------

        try {
            UnitTestHelperException.assertThrows(undefinedError, () -> log.info("no error"));
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("method must throw exception");
        }

        try {
            UnitTestHelperException.assertThrows(undefinedError, () -> {
                throw new UncheckedException(DefaultErrorCode.buildUndefineErrorCode().errorCode("ERR").build());
            });
            throw new Error("should throw");
        } catch (final Throwable e) {
            if (e instanceof Error) {
                throw e;
            }
            assertThat(e.getMessage()).isEqualTo("current : \"ERR\"  ref: \"err-undefine\"");
        }

    }


    @Test
    public void throwException_nominal() {
        UnitTestHelperException.assertThrows(NullPointerException.class, "sorry", () -> {
            UnitTestHelperException.throwException(new NullPointerException("sorry"));
        });
    }


}