import React, { useState } from "react";
import type UserProfileType from "../../../../../lib/types/entity_types/UserProfileType";
import {
  Button,
  Space,
  Title,
  Text,
  Group,
  Stack,
  TextInput,
  Center,
  SimpleGrid,
} from "@mantine/core";
import {
  IconArrowLeft,
  IconArrowRight,
  IconHomeCog,
  IconKey,
} from "@tabler/icons-react";

export default function UserSetupForm() {
  const [activeStep, setActiveStep] = useState(0);

  const [errors, setErrors] = useState<{
    firstName?: string;
    lastName?: string;
  }>({});

  const [formData, setFormData] = useState<Partial<UserProfileType>>({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    profilePictureUrl: "",
    profileType: "tenant",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  // Validation function
  const validatePersonalDetails = () => {
    const newErrors: { firstName?: string; lastName?: string } = {};
    if (!formData.firstName?.trim())
      newErrors.firstName = "First name is required";
    if (!formData.lastName?.trim())
      newErrors.lastName = "Last name is required";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  return (
    <form>
      <Title mb="xs" ta="left" w={"100%"}>
        Step {activeStep + 1} of 3:
      </Title>
      <Text size="md" ta="left">
        {activeStep === 0
          ? "Select a profile type."
          : activeStep === 1
          ? "Please fill out some personal information."
          : "Review and confirm your details."}
      </Text>
      <Space h="md" />
      {activeStep == 0 && (
        <SimpleGrid cols={2} spacing={"md"}>
          <Center>
            <Button
              h={150}
              w={150}
              variant="default"
              onClick={() => {
                setFormData((prev) => ({
                  ...prev,
                  profileType: "tenant",
                }));
                setActiveStep(1);
              }}
            >
              <Stack align="center" justify="center" gap={10}>
                <IconKey size={40} />
                <Text size="sm" style={{ textWrap: "wrap" }}>
                  Renter
                </Text>
              </Stack>
            </Button>
          </Center>
          <Center>
            <Button
              h={150}
              w={150}
              variant="default"
              onClick={() => {
                setFormData((prev) => ({
                  ...prev,
                  profileType: "propertyManager",
                }));
                setActiveStep(1);
              }}
            >
              <Stack align="center" justify="center" gap={10}>
                <IconHomeCog size={40} />
                <Text size="sm" style={{ textWrap: "wrap" }}>
                  Property Manager
                </Text>
              </Stack>
            </Button>
          </Center>
        </SimpleGrid>
      )}
      {activeStep == 1 && (
        <Stack>
          <TextInput
            label="First Name"
            placeholder="Riley"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            type="text"
            error={errors.firstName}
            withAsterisk
          />
          <TextInput
            label="Last Name"
            placeholder="Joseph"
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            type="text"
            error={errors.lastName}
            withAsterisk
          />
          <TextInput
            label="Phone Number"
            placeholder="(123) 456-7890"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={(e) => {
              const raw = e.target.value.replace(/\D/g, "");
              let formatted = raw;
              if (raw.length > 0) {
                formatted = `(${raw.slice(0, 3)}`;
              }
              if (raw.length >= 4) {
                formatted += `) ${raw.slice(3, 6)}`;
              }
              if (raw.length >= 7) {
                formatted += ` ${raw.slice(6, 10)}`;
              }
              e.target.value = formatted;
              handleChange(e);
            }}
            type="tel"
            maxLength={14}
          />
        </Stack>
      )}
      <Space h="sm" />
      {activeStep > 0 && (
        <Group justify="space-between" align="center">
          <Button
            variant="transparent"
            p={0}
            onClick={() => {
              if (activeStep !== 0) {
                setActiveStep(activeStep - 1);
                if (activeStep === 1) {
                  setErrors({});
                }
              }
            }}
          >
            <Group gap={5} justify="space-between">
              <IconArrowLeft size={20} />
              <Text size="sm">Back</Text>
            </Group>
          </Button>
          {activeStep > 0 && activeStep < 2 && (
            <Button
              variant="transparent"
              p={0}
              onClick={() => {
                if (activeStep === 1) {
                  if (validatePersonalDetails()) {
                    setActiveStep(activeStep + 1);
                  }
                } else if (activeStep !== 2) {
                  setActiveStep(activeStep + 1);
                }
              }}
            >
              <Group gap={5} justify="space-between">
                <Text size="sm">Next</Text>
                <IconArrowRight size={20} />
              </Group>
            </Button>
          )}
        </Group>
      )}
    </form>
  );
}
